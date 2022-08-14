export default {
  state: {
    status: "matching", //matching or playing
    socket: null,
    opponent_username: "zz",
    opponent_photo: "",
    gameMap: null,
  },
  getters: {},
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOpponent(state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
    },
    updateStatus(state, status) {
      state.status = status;
    },
    updateGameMap(state, gameMap) {
      state.gameMap = gameMap;
    },
  },
  actions: {},
  modules: {},
};
