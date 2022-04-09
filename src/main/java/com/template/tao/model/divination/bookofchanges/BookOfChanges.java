package com.template.tao.model.divination.bookofchanges;

import com.template.tao.model.divination.energy.Form;
import com.template.tao.model.divination.energy.Phenomena;
import com.template.tao.utils.BitUtils;
import com.template.tao.utils.CoinUtils;
import com.template.tao.utils.ConnectionUtils;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.BitSet;
import java.util.stream.Collectors;

import static com.template.tao.model.divination.bookofchanges.Interpretation.*;

@RequiredArgsConstructor
public class BookOfChanges {

  private static final List<Hexagram> BOOK = createBook();
  private static final List<Integer> LINE_INDEX_MAP = BOOK.sortBy(Hexagram::getValue).map(Hexagram::getIndex);
  private static final Map<Hexagram, HexagramGroup> SYMMETRY_MAP = createSymmetryMap();
  public static final List<HexagramGroup> SYMMETRIC_GROUPS = SYMMETRY_MAP.values().distinct().toList();
  private static final Map<String, Interpretation> INTERPRETATION_TEXT_MAP = getInterpretationTextMap();
  private static final Map<Interpretation, String> INTERPRETATIONS = getInterpretations();

  public DivinationResult cast(String question, Interpretation interpretation) {
    BitSet first = new BitSet(Hexagram.NUM_LINES);
    BitSet changes = new BitSet(Hexagram.NUM_LINES);
    for (int i = 0; i < Hexagram.NUM_LINES; i++) {
      BitSet coins = CoinUtils.toss(4);
      if (coins.get(0)) first.set(i);
      if (coins.cardinality() == 3) changes.set(i);
    }
    String resultString = this.toResultString(first, changes);
    return getResultInternal(question, interpretation, first, changes, resultString);
  }

  public DivinationResult lookupResult(String question, Interpretation interpretation, String resultString) {
    BitSet first = new BitSet(Hexagram.NUM_LINES);
    BitSet changes = new BitSet(Hexagram.NUM_LINES);
    List.ofAll(resultString.chars().mapToObj(v -> Phenomena.fromValue(v - '0')))
        .forEachWithIndex((phenomena, i) -> {
          if (phenomena.getForm() == Form.YANG) first.set(i);
          if (phenomena.isChange()) changes.set(i);
        });
    return getResultInternal(question, interpretation, first, changes, resultString);
  }

  private DivinationResult getResultInternal(String question,
                                             Interpretation interpretation,
                                             BitSet first,
                                             BitSet changes,
                                             String resultString) {
    return Tuple.of(BookOfChanges.fromLineValue(BitUtils.asInt(first)))
        .apply(current -> new DivinationResult.DivinationResultBuilder()
            .question(question)
            .value(resultString)
            .current(current)
            .future(this.getTransitionalFuture(current, changes))
            .movingLines(this.getMovingLines(changes))
            .interpretation(this.getInterpretation(interpretation))
            .build());
  }

  private String toResultString(BitSet first, BitSet changes) {
    return List.range(0, Hexagram.NUM_LINES)
        .reverse()
        .map(i -> Tuple.of(first.get(i) ? 1 : 0)
            .apply(v -> ((changes.get(i) ? v : 1 - v) << 1) + v))
        .map(i -> String.valueOf(Phenomena.values()[i].getValue()))
        .collect(Collectors.joining());
  }

  private List<Hexagram> getTransitionalFuture(Hexagram current, BitSet changes) {
    return List.range(0, Hexagram.NUM_LINES)
        .filter(changes::get)
        .foldLeft(List.of(current), (ls, i) ->
            ls.append(fromLineValue(ls.last().getValue() ^ (1 << i))))
        .tail();
  }

  private List<Integer> getMovingLines(BitSet changes) {
    return List.range(0, Hexagram.NUM_LINES)
        .filter(changes::get)
        .map(i -> i + 1);
  }

  private String getInterpretation(Interpretation interpretation) {
    return Option.of(interpretation)
        .flatMap(INTERPRETATIONS::get)
        .orElse(INTERPRETATIONS.get(DEFAULT))
        .get();
  }

  public Hexagram lookupHexagram(int sequenceIndex) {
    return fromSequenceIndex(sequenceIndex);
  }

  public static Hexagram fromSequenceIndex(int index) {
    return Try.of(() -> BOOK.get(index - 1)).getOrNull();
  }

  public static Hexagram fromLineValue(int value) {
    return fromSequenceIndex(LINE_INDEX_MAP.get(value));
  }

  private static List<Hexagram> createBook() {
    return fetchSectionTable("Interpretation")
        .zip(fetchSectionTable("The_Moving_Line"))
        .zip(fetchSectionTable("Explanation_Of_I_Ching_Hexagrams_And_Lines"))
        .zipWithIndex((t, i) -> Tuple.of(t._1._1, t._1._2, t._2, i))
        .map(t -> t.apply((interpretation, line, explanation, index) ->
            new Hexagram.HexagramBuilder()
                .index(index + 1)
                .value(Integer.parseInt(line.get(2).childNodes().get(0).toString().trim(), 2))
                .name(String.format("%s %s", explanation.get(0).childNodes().get(0).toString().trim(),
                    explanation.get(0).select("p").get(0).text().trim()))
                .judgement(interpretation.get(2).wholeText().trim())
                .image(interpretation.get(3).wholeText().trim())
                .lines(List.of(line.get(2).select("p").get(0)
                    .select("b").before("|").parents().get(0).wholeText().trim()
                    .split("\\|")).tail())
                .judgementComment(List.ofAll(explanation.get(0).select("p"))
                    .tail().map(e -> e.wholeText().trim())
                    .collect(Collectors.joining("\n")))
                .imageComment(explanation.get(1).select("p").get(0).wholeText().trim())
                .lineComments(List.ofAll(explanation.get(1).select("li"))
                    .map(e -> e.wholeText().trim()))
                .build()));
  }

  private static List<List<Element>> fetchSectionTable(String section) {
    return ConnectionUtils.get("https://en.wikibooks.org/wiki/I_Ching/" + section)
//        .map(document -> document.getElementsByTag("table").get(0))
        .map(document -> List.ofAll(document.select("td"))
            .grouped(document.select("th").size())
            .toList())
        .get();
  }

  private static Map<String, Interpretation> getInterpretationTextMap() {
    return HashMap.<String, Interpretation>empty()
        .put("Master Yin", ALFRED)
        .put("Wei Tat", WEI_TAT)
        .put("Ritsema and Karcher", KARCHER)
        .put("Wilhelm and Blofeld", WILHELM)
        .put("Gregory Whincup", GREGORY)
        .put("Henry Wei", HENRY)
        .put("Kerson  Huang", KERSON)
        .put("The Nanjing rules", NANJING)
        .put("Chu Hsi’s rules", CHU_HSI);
  }

  private static Map<Interpretation, String> getInterpretations() {
    return List.ofAll(ConnectionUtils
            .get("http://www.russellcottrell.com/virtualyarrowstalks/rules.htm")
            .get().body().children())
        .foldLeft(List.of(List.<Element>empty()), (acc, e) -> "br".equals(e.tagName())
            ? acc.append(List.empty())
            : acc.splitAt(acc.size() - 1)
            .map2(lastList -> lastList.map(last -> last.append(e)))
            .apply(List::appendAll))
        .transform(ls -> ls.subSequence(1, ls.size() - 1))
        .map(es -> es.splitAt(1)
            .map1(hs -> INTERPRETATION_TEXT_MAP.get(hs.get().wholeText().trim()).orNull())
            .map2(ts -> Jsoup.parse(ts.map(Element::toString).collect(Collectors.joining())).wholeText().trim()))
        .toMap(Tuple2::_1, Tuple2::_2);
  }

  private static Map<Hexagram, HexagramGroup> createSymmetryMap() {
    return BOOK
        .foldLeft(Tuple.of(HashMap.<Hexagram, HexagramGroup>empty(), HashSet.empty()),
            (t, s) -> t._2.contains(s) ? t : Tuple.of(new HexagramGroup(s.getSymmetries()))
                .apply(ss -> Tuple.of(
                    ss.getGroup().foldLeft(t._1, (m, h) -> m.put(h.getHexagram(), ss)),
                    t._2.addAll(ss.getGroup()))))
        ._1;
  }

  public static HexagramGroup getSymmetryGroup(Hexagram hexagram) {
    return SYMMETRY_MAP.getOrElse(hexagram, HexagramGroup.NULL);
  }
}
