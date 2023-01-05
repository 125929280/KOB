<template>
  <ContentField v-if="!$store.state.user.pulling_info">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
          </div>
          <div class="mb-3">
            <label for="verificationCode" class="form-label">验证码</label>
            <input
              v-model="verificationCode"
              type="text"
              class="form-control"
              id="verificationCode"
              placeholder="请输入验证码"
            />
            <div>
              <img
                :src="imgUrl"
                alt="点击刷新"
                class="picture"
                @click="refresh($event)"
              />
            </div>
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">登录</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>
<script>
import ContentField from "../../../components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "../../../router/index";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const imgUrl = ref(
      "http://localhost:3000/user/account/getVerificationCode"
    );
    let username = ref("");
    let password = ref("");
    let verificationCode = ref("");
    let error_message = ref("");

    const jwt_token = localStorage.getItem("jwt_token");
    if (jwt_token) {
      store.commit("updateToken", jwt_token);
      store.dispatch("getInfo", {
        success() {
          router.push({ name: "home" });
          store.commit("updatePullingInfo", false);
        },
        error() {
          store.commit("updatePullingInfo", false);
        },
      });
    } else {
      store.commit("updatePullingInfo", false);
    }

    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        verificationCode: verificationCode.value,
        success() {
          store.dispatch("getInfo", {
            success() {
              router.push({ name: "home" });
            },
          });
        },
        error(resp) {
          error_message.value = resp.error_message;
        },
      });
    };

    const refresh = (event) => {
      event.target.src = `${imgUrl.value}?t=${new Date().getTime()}`;
      console.log(event.target.src);
    };

    return {
      username,
      password,
      verificationCode,
      error_message,
      imgUrl,
      login,
      refresh,
    };
  },
};
</script>
<style scoped>
button {
  width: 100%;
}
div.error-message {
  color: red;
}
</style>
