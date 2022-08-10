<template>
  <ContentField>我的Bot</ContentField>
</template>
<script>
import ContentField from "../../../components/ContentField.vue";
import $ from "jquery";
import { useStore } from "vuex";

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/add/",
    //   type: "post",
    //   data: {
    //     title: "title.value",
    //     description: "description.value",
    //     content: "content.value",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });

    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/remove/",
    //   type: "post",
    //   data: {
    //     bot_id: 3,
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });

    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/update/",
    //   type: "post",
    //   data: {
    //     bot_id: 1,
    //     title: "title.value1",
    //     description: "description.value1",
    //     content: "content.value1",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });

    $.ajax({
      url: "http://127.0.0.1:3000/user/bot/getList/",
      type: "get",
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
  },
};
</script>
<style scoped></style>
