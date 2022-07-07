
const battleBackgroundImage = new Image()
battleBackgroundImage.src = '/static/img/battleBackground.png'

const battleBackground = new Sprite({
  position: {
    x: 0,
    y: 0
  },
  image: battleBackgroundImage
})

let enemy
let character
let renderedSprites
let battleAnimationId
let queue

function handleBattleEnded() {
  // fade back to black
  gsap.to('#overlappingDiv', {
    opacity: 1,
    onComplete: () => {
      // cancelAnimationFrame(battleAnimationId)
      gameState.setUiState(UiState.town)
      animate()
      document.querySelector('#userInterface').style.display = 'none'

      gsap.to('#overlappingDiv', {
        opacity: 0
      })

      // battle.initiated = false
      audio.Map.play()
    }
  })
}

function initBattle() {
  document.querySelector('#userInterface').style.display = 'block'
  document.querySelector('#dialogueBox').style.display = 'none'
  document.querySelector('#enemyHealthBar').style.width = '100%'
  document.querySelector('#characterHealthBar').style.width = '100%'
  document.querySelector('#attacksBox').replaceChildren()

  enemy = new Monster(monsters.Draggle)
  character = new Monster(monsters.Emby)
  renderedSprites = [enemy, character]
  queue = []

  character.attacks.forEach((attack) => {
    const button = document.createElement('button')
    button.innerHTML = attack.name
    document.querySelector('#attacksBox').append(button)
  })

  // our event listeners for our buttons (attack)
  document.querySelectorAll('button').forEach((button) => {
    button.addEventListener('click', (e) => {
      const selectedAttack = attacks[e.currentTarget.innerHTML]
      character.attack({
        attack: selectedAttack,
        recipient: enemy,
        renderedSprites
      })

      if (enemy.health <= 0) {
        queue.push(() => enemy.faint())
        queue.push(handleBattleEnded)
      }

      // draggle or enemy attacks right here
      const randomAttack =
        enemy.attacks[Math.floor(Math.random() * enemy.attacks.length)]

      queue.push(() => {
        enemy.attack({
          attack: randomAttack,
          recipient: character,
          renderedSprites
        })

        if (character.health <= 0) {
          queue.push(() => character.faint())
          queue.push(handleBattleEnded)
        }
      })
    })

    button.addEventListener('mouseenter', (e) => {
      const selectedAttack = attacks[e.currentTarget.innerHTML]
      document.querySelector('#attackType').innerHTML = selectedAttack.type
      document.querySelector('#attackType').style.color = selectedAttack.color
    })
  })
}

function animateBattle() {
  gameState.setUiState(UiState.battle)
  battleAnimationId = gameState.getAnimationId()
  // battleAnimationId = window.requestAnimationFrame(animateBattle)
  battleBackground.draw()

  renderedSprites.forEach((sprite) => {
    sprite.draw()
  })
}
UiState.battle.animation = animateBattle

animate()
// initBattle()
// animateBattle()

document.querySelector('#dialogueBox').addEventListener('click', (e) => {
  if (queue.length > 0) {
    queue[0]()
    queue.shift()
  } else e.currentTarget.style.display = 'none'
})
