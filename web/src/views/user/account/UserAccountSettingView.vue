<template>
  <ContentField></ContentField>
</template>
<script>
import ContentField from "../../../components/ContentField.vue";
import $ from "jquery";
import { ref } from "vue";
import { useStore } from "vuex";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let error_message = ref("");
    $.ajax({
      url: "http://127.0.0.1:3000/user/account/updatePassword/",
      type: "post",
      data: {
        oldPassword: "12345",
        password: "123",
        confirmedPassword: "123",
      },
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
    return {
      error_message,
    };
  },
};
</script>
<style scoped></style>
