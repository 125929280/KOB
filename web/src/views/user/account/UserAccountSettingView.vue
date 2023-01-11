<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-5">
        <!-- <h3 class="text-left text-info border-bottom pd-2">更换头像</h3> -->
        <div class="input-group">
          <input
            type="file"
            class="form-control"
            id="photo"
            aria-describedby="photo"
            aria-label="Upload"
          />
          <button @click="update_photo()" class="btn btn-primary" type="button">
            更换头像
          </button>
        </div>
        <form @submit.prevent="update_password">
          <!-- <h3 class="text-left text-info border-bottom pd-2">更换密码</h3> -->
          <div class="mb-3">
            <label for="oldPassword" class="form-label">旧密码</label>
            <input
              v-model="oldPassword"
              type="password"
              class="form-control"
              id="oldPassword"
              placeholder="请输入旧密码"
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
            <label for="confirmedPassword" class="form-label">确认密码</label>
            <input
              v-model="confirmedPassword"
              type="password"
              class="form-control"
              id="confirmedPassword"
              placeholder="请再次输入密码"
            />
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">更换密码</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>
<script>
import ContentField from "../../../components/ContentField.vue";
import $ from "jquery";
import { ref } from "vue";
import { useStore } from "vuex";
import router from "../../../router/index";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let oldPassword = ref("");
    let password = ref("");
    let confirmedPassword = ref("");
    let error_message = ref("");
    let photo = ref("");
    const update_password = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/updatePassword/",
        type: "post",
        data: {
          oldPassword: oldPassword.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            error_message.value = "";
            router.push({ name: "home" });
            // router.push({ name: "user_account_login" });
          } else {
            error_message.value = resp.error_message;
          }
        },
        error(resp) {
          error_message.value = resp.error_message;
          console.log(resp);
        },
      });
    };

    const update_photo = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/updatePhoto/",
        type: "post",
        data: {
          photo: photo.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            error_message.value = "";
            router.push({ name: "home" });
            // router.push({ name: "user_account_login" });
          } else {
            error_message.value = resp.error_message;
          }
        },
        error(resp) {
          error_message.value = resp.error_message;
          console.log(resp);
        },
      });
    };

    return {
      oldPassword,
      password,
      confirmedPassword,
      error_message,
      update_password,
      update_photo,
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
