define((require, exports, module) => {
  const Robot = require('Robot')
  const RobotTHREE = require('RobotTHREE')
  const RobotGui = require('Robot.Gui')
  const Target = require('Target')
  const TargetGui = require('Target.Gui')
  const gui = require('UiDat')
  const THREEView = require('THREEView')
  const storeManager = require('State')
  const ws = require('WorkingSpace')
  const Path = require('Path.Gui')
  const Kinematic = require('Kinematic')
  // const RemoteRobot = require('RemoteRobot')

  const logger = store => dispatch => (action, data) => {
    console.group(`ACTION ${action}`)

    console.log(`action: %c${action}`, 'color:green')
    console.log('data: ', data)
    console.log('%cstore before: ', 'color:orange', store.getState())

    const newState = dispatch(action, data)
    console.log('%cnew state: ', 'color:green', newState)
    console.groupEnd()
    return newState
  }

  const mid = store => dispatch => (action, data) => {
    const oldState = store.getState()
    const oldStateCopy = JSON.parse(JSON.stringify(oldState))

    const newState = dispatch(action, data)

    function compare(o, n, os) {
      for (const i of Object.keys(o).concat(Object.keys(n))) {
        if (typeof n[i] === 'undefined') {
          if (os === n) {
            console.warn('nooohohoohoh did not change state, bro!')
            console.warn('element was removed, but parent not changed')
          }
        } else if (typeof o[i] === 'undefined') {
          if (os === n) {
            console.warn('nooohohoohoh did not change state, bro!')
            console.warn('element was added, but parent not changed')
          }
        } else if (!!o[i] && typeof (o[i]) === 'object') {
          // console.log('aaaa')
          //
          compare(o[i], n[i], os[i])
        } else {
          if (typeof n[i] === 'undefined' || o[i] !== n[i]) { // el deleted, or value not same
            // value has changed todo iter over newState (missing ones were deleted, dont matter. new ones dont matter either hm....)

            // new state cant be old state, if a child changed
            if (os === n) {
              console.warn('nooohohoohoh did not change state, bro!')
              console.group(`state ${action}`)
              console.log(`oldStateCopy: ${o[i]}`)
              console.log(`oldState: %c${os[i]}`, 'color: red')
              console.log(`newState: ${n[i]}`)
              console.groupEnd()
            }
          }
          // console.log(i, o[i] === n[i])
        }
      }
    }
    compare(oldStateCopy, newState, oldState)

    return newState
  }

  storeManager.applyMiddleware(logger, mid)
  /* POLYFILL */

  const reduce = Function.bind.call(Function.call, Array.prototype.reduce)
  const isEnumerable = Function.bind.call(Function.call, Object.prototype.propertyIsEnumerable)
  const concat = Function.bind.call(Function.call, Array.prototype.concat)
  const keys = Reflect.ownKeys

  if (!Object.values) {
    Object.values = function values(O) {
      return reduce(keys(O), (v, k) => concat(v, typeof k === 'string' && isEnumerable(O, k) ? [O[k]] : []), [])
    }
  }

  /* END POLYFILL */

  class Hmi {
    constructor(API_URL, sessionKey) {
      const maxAngleVelocity = 90.0 / (180.0 * Math.PI) / 1000.0

      const store = storeManager.createStore('Hmi', {})

      const scope = this

      /* THREEJS SCENE SETUP */

      const {
        scene,
        renderer,
        camera,
      } = require('THREEScene')
      this.scene = scene
      this.renderer = renderer
      this.camera = camera


      /*Robot.dispatch('ROBOT_CHANGE_ANGLES', {
        A0: angles[0],
        A1: angles[1],
        A2: angles[2],
        A3: angles[3],
        A4: angles[4],
        A5: angles[5],
      })*/

      async function updateAngles() {
        const res = await fetch(API_URL + '/session/' + sessionKey + '/attribute/JOINT_POSITIONS', {
          method: 'GET',
          mode: 'cors'
        });
        const json = await res.json();
        console.log(json);
        const firstInstance = Object.values(json)[0];
        if (firstInstance != null) {
          Robot.dispatch('ROBOT_CHANGE_ANGLES', {
            A0: firstInstance.jointPositions[0],
            A1: firstInstance.jointPositions[1],
            A2: firstInstance.jointPositions[2],
            A3: firstInstance.jointPositions[3],
            A4: firstInstance.jointPositions[4],
            A5: firstInstance.jointPositions[5],
          });
        }
      }
      updateAngles();
      setInterval(() => {
        updateAngles();
      }, 2000);

      const geo = [
        [-4.8, -4.8, 2.1],
        [-0.6, 0.5, 10.0],
        [1.4, -0.6, 1.4],
        [10, 0, 0],
        [3, 0, 0],
        [0, 0, 0],
      ]

      Robot.dispatch('ROBOT_CHANGE_GEOMETRY', {
        V0: {
          x: geo[0][0],
          y: geo[0][1],
          z: geo[0][2],
        },
        V1: {
          x: geo[1][0],
          y: geo[1][1],
          z: geo[1][2],
        },
        V2: {
          x: geo[2][0],
          y: geo[2][1],
          z: geo[2][2],
        },
        V3: {
          x: geo[3][0],
          y: geo[3][1],
          z: geo[3][2],
        },
        V4: {
          x: geo[4][0],
          y: geo[4][1],
          z: geo[4][2],
        }
      });

      /* END THREEJS SCENE SETUP */

      /* DAT GUI */

      const hmiGui = gui.addFolder('HMI')
      gui.remember(scope.state)

      const fun = {
        resetTargetAngles: () => {
          Robot.dispatch('ROBOT_CHANGE_ANGLES', {
            A0: 0,
            A1: 0,
            A2: 0,
            A3: 0,
            A4: 0,
            A5: 0,
          })
        },
      }

      hmiGui.add(fun, 'resetTargetAngles').onChange(() => {

      })
      window.debug.show = false
      hmiGui.add(window.debug, 'show')

      /* CONNECT MODULES */
    }
  }

  module.exports = Hmi
})
