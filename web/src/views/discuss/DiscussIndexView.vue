<template>
  <ContentField>
    <!-- <span style="font-size: 200%">讨论列表</span>
    <button
      type="button"
      class="btn btn-primary float-end"
      data-bs-toggle="modal"
      data-bs-target="#add-discuss-btn"
    >
      发表讨论
    </button> -->
    <!-- Modal -->
    <!-- <div class="modal fade" id="add-discuss-btn" tabindex="-1">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">发表讨论</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="add-discuss-title" class="form-label">标题</label>
              <input
                v-model="discuss_add.title"
                type="text"
                class="form-control"
                id="add-bot-title"
                placeholder="请输入讨论标题"
              />
            </div>
            <div class="mb-3">
              <label for="add-discuss-content" class="form-label">内容</label>
              <textarea
                v-model="discuss_add.content"
                class="form-control"
                id="add-discuss-content"
                rows="3"
                placeholder="请输入讨论内容"
              ></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <div class="error-message">{{ discuss_add.error_message }}</div>
            <button type="button" class="btn btn-primary" @click="add_discuss">
              创建
            </button>
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              取消
            </button>
          </div>
        </div>
      </div>
    </div> -->
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>类型</th>
          <th>标题</th>
          <th>作者</th>
          <th>时间</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="discuss in discusses" :key="discuss.discuss.id">
          <td>
            {{ discuss.discuss.type }}
          </td>
          <td>
            {{ discuss.discuss.title }}
          </td>
          <td>
            <img :src="discuss.photo" alt="" class="discuss-user-photo" />
            &nbsp;
            <span class="discuss-user-username">{{ discuss.username }}</span>
          </td>
          <td>
            {{ discuss.discuss.createTime }}
          </td>
        </tr>
      </tbody>
    </table></ContentField
  >
</template>
<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let discusses = ref([]);
    let current_page = 1;
    let total_discusses = 0;
    console.log(total_discusses);

    const pull_page = (page) => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/discuss/getList/",
        data: {
          page,
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          discusses.value = resp.discusses;
          total_discusses = resp.discusses_count;
          //   update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    pull_page(current_page);
    return {
      discusses,
    };
    // $.ajax({
    //   url: "http://127.0.0.1:3000/discuss/add/",
    //   type: "post",
    //   data: {
    //     title: "bot_add.title",
    //     type: "BLOG",
    //     content: "bot_add.content",
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
    //   url: "http://127.0.0.1:3000/discuss/remove/",
    //   type: "post",
    //   data: {
    //     discuss_id: 4,
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
    //   url: "http://127.0.0.1:3000/discuss/getList/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   data: {
    //     page: 1,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });

    // $.ajax({
    //   url: "http://127.0.0.1:3000/discuss/update/",
    //   type: "post",
    //   data: {
    //     discuss_id: 4,
    //     title: "bot_add.title",
    //     type: "SOLUTION",
    //     content: "bot_add.content",
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
  },
};
</script>
<style scoped>
img.discuss-user-photo {
  width: 4vh;
  border-radius: 50%;
}
</style>
