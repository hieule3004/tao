
// setup canvas
const canvas = document.querySelector('canvas')
const c = canvas.getContext('2d')
canvas.width = 1024
canvas.height = 576

const imageWidth = 70;
const offset = {
  x: -735,
  y: -650
}

//setup game state
const gameState = new GameState({})

const boundaries = []
collisions.forEach((v, p) => {
  if (v === 0) return;
  boundaries.push(new Boundary({
    position: {
      x: (p % imageWidth) * pixel.width + offset.x,
      y: (p / imageWidth >> 0) * pixel.height + offset.y
    }
  }))
})

const battleZones = []
battleZonesData.forEach((v, p) => {
  if (v === 0) return;
  battleZones.push(new Boundary({
    position: {
      x: (p % imageWidth) * pixel.width + offset.x,
      y: (p / imageWidth >> 0) * pixel.height + offset.y
    }
  }))
})

const backgroundImage = new Image()
backgroundImage.src = '/static/img/Pellet Town.png'

const foregroundImage = new Image()
foregroundImage.src = '/static/img/foregroundObjects.png'

const playerDownImage = new Image()
playerDownImage.src = '/static/img/playerDown.png'

const playerUpImage = new Image()
playerUpImage.src = '/static/img/playerUp.png'

const playerLeftImage = new Image()
playerLeftImage.src = '/static/img/playerLeft.png'

const playerRightImage = new Image()
playerRightImage.src = '/static/img/playerRight.png'

const player = new Sprite({
  position: {
    x: canvas.width / 2 - 192 / 4 / 2,
    y: canvas.height / 2 - 68 / 2
  },
  image: playerDownImage,
  frames: {
    max: 4,
    hold: 10
  },
  sprites: {
    up: playerUpImage,
    left: playerLeftImage,
    right: playerRightImage,
    down: playerDownImage
  }
})

const background = new Sprite({
  position: {
    x: offset.x,
    y: offset.y
  },
  image: backgroundImage
})

const foreground = new Sprite({
  position: {
    x: offset.x,
    y: offset.y
  },
  image: foregroundImage
})

let keys = {
  w: {direction: 'up', offset: {x: 0, y: 3}, pressed: false},
  a: {direction: 'left', offset: {x: 3, y: 0}, pressed: false},
  s: {direction: 'down', offset: {x: 0, y: -3}, pressed: false},
  d: {direction: 'right', offset: {x: -3, y: 0}, pressed: false}
}
let lastKey = null

const movables = [background, ...boundaries, ...battleZones, foreground]

function rectangularCollision({rectangle1, rectangle2}) {
  return (
    rectangle1.position.x + rectangle1.width >= rectangle2.position.x &&
    rectangle1.position.x <= rectangle2.position.x + rectangle2.width &&
    rectangle1.position.y <= rectangle2.position.y + rectangle2.height &&
    rectangle1.position.y + rectangle1.height >= rectangle2.position.y
  )
}

function animate() {
  gameState.setUiState(UiState.town)
  // const animationId = window.requestAnimationFrame(animate)
  const animationId = gameState.getAnimationId()

  background.draw()
  boundaries.forEach(boundary => boundary.draw())
  battleZones.forEach(battleZone => battleZone.draw())
  player.draw()
  foreground.draw()

  let moving = true
  player.animate = false

  if (gameState.uiState !== UiState.town) return

  // activate a battle
  if (Object.values(keys).some(movementKey => movementKey.pressed)) {
    for (let i = 0; i < battleZones.length; i++) {
      const battleZone = battleZones[i]
      const overlappingArea =
        (Math.min(
            player.position.x + player.width,
            battleZone.position.x + battleZone.width
          ) -
          Math.max(player.position.x, battleZone.position.x)) *
        (Math.min(
            player.position.y + player.height,
            battleZone.position.y + battleZone.height
          ) -
          Math.max(player.position.y, battleZone.position.y))
      if (
        rectangularCollision({
          rectangle1: player,
          rectangle2: battleZone
        }) &&
        overlappingArea > (player.width * player.height) / 2 &&
        Math.random() < 0.1
      ) {
        // deactivate current animation loop
        gameState.setUiState(UiState.battle)
        // window.cancelAnimationFrame(animationId)
        // gameState.uiState = UiState.battle

        audio.Map.stop()
        audio.initBattle.play()
        audio.battle.play()

        gsap.to('#overlappingDiv', {
          opacity: 1,
          repeat: 3,
          yoyo: true,
          duration: 0.4,
          onComplete() {
            gsap.to('#overlappingDiv', {
              opacity: 1,
              duration: 0.4,
              onComplete() {
                // activate a new animation loop
                initBattle()
                animateBattle()
                gsap.to('#overlappingDiv', {
                  opacity: 0,
                  duration: 0.4
                })
              }
            })
          }
        })
        break
      }
    }
  }

  if (lastKey in keys && keys[lastKey].pressed) {
    player.animate = true
    player.image = player.sprites[keys[lastKey].direction]
    let movementOffset = keys[lastKey].offset

    for (let i = 0; i < boundaries.length; i++) {
      const boundary = boundaries[i]
      if (
        rectangularCollision({
          rectangle1: player,
          rectangle2: {
            ...boundary,
            position: {
              x: boundary.position.x + movementOffset.x,
              y: boundary.position.y + movementOffset.y
            }
          }
        })
      ) {
        moving = false
        break
      }
    }

    if (moving)
      movables.forEach((movable) => {
        movable.position.x += movementOffset.x
        movable.position.y += movementOffset.y
      })
  }
}
UiState.town.animation = animate

// animate()

window.addEventListener('keydown', (e) => {
  if (e.key in keys) {
    keys[e.key].pressed = true
    lastKey = e.key
  }
})

window.addEventListener('keyup', (e) => {
  if (e.key in keys)
    keys[e.key].pressed = false
})

let clicked = false
addEventListener('click', () => {
  if (!clicked) {
    audio.Map.play()
    clicked = true
  }
})
