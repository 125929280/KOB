<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
      Draw
    </div>
    <div
        class="result-board-text"
        v-else-if="
        $store.state.pk.loser === 'a' &&
        $store.state.pk.a_id == parseInt($store.state.user.id)
        // 不能用=== 因为类型不一样
      "
    >
      Lose
    </div>
    <div
        class="result-board-text"
        v-else-if="
        $store.state.pk.loser === 'b' &&
        $store.state.pk.b_id === parseInt($store.state.user.id)
      "
    >
      Lose
    </div>
    <div class="result-board-text" v-else>Win</div>
    <div class="result-board-btn">
      <button type="button" class="btn btn-warning btn-lg" @click="restart">
        再来！
      </button>
    </div>
  </div>
</template>
<script>
import {useStore} from "vuex";

export default {
  setup() {
    const store = useStore();

    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateLoser", "none");
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "http://127.0.0.1:3000/anonymous.png",
      });
    };

    return {
      restart,
    };
  },
};
</script>
<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
}

div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}

div.result-board-btn {
  text-align: center;
  padding-top: 7vh;
}
</style>
