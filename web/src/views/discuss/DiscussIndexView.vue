<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>类型</th>
          <th>标题</th>
          <th>作者</th>
          <th>发表时间</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="discuss in discusses" :key="discuss.discuss.id">
          <td>
            {{ discuss.type }}
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
          <td>
            <button
              type="button"
              class="btn btn-primary"
              data-bs-toggle="modal"
              :data-bs-target="'#open-discuss-btn-' + discuss.discuss.id"
              @click="
                pull_comments_page(current_comments_page, discuss.discuss.id)
              "
            >
              查看
            </button>
            <!-- Modal -->
            <div
              class="modal fade"
              :id="'open-discuss-btn-' + discuss.discuss.id"
              tabindex="-1"
            >
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">讨论</h5>
                    <button
                      type="button"
                      class="btn-close"
                      data-bs-dismiss="modal"
                      aria-label="Close"
                    ></button>
                  </div>
                  <div class="modal-body">
                    <table
                      class="table table-striped table-hover"
                      style="text-align: center"
                    >
                      <thead>
                        <tr>
                          <th>作者</th>
                          <th>内容</th>
                          <th>发表时间</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>
                            <img
                              :src="discuss.photo"
                              alt=""
                              class="discuss-user-photo"
                            />
                            &nbsp;
                            <span class="discuss-user-username">{{
                              discuss.username
                            }}</span>
                          </td>
                          <td>
                            {{ discuss.discuss.content }}
                          </td>
                          <td>
                            {{ discuss.discuss.createTime }}
                          </td>
                        </tr>
                      </tbody>
                      <tbody>
                        <tr
                          v-for="comment in comments"
                          :key="comment.comment.id"
                        >
                          <td>
                            <img
                              :src="comment.photo"
                              alt=""
                              class="comment-user-photo"
                            />
                            &nbsp;
                            <span class="comment-user-username">{{
                              comment.username
                            }}</span>
                          </td>
                          <td>
                            {{ comment.comment.content }}
                          </td>
                          <td>
                            {{ comment.comment.createTime }}
                          </td>
                          <td>
                            <button
                              @click="
                                remove_comment(
                                  comment.comment.id,
                                  discuss.discuss.id
                                )
                              "
                              type="button"
                              class="btn btn-danger"
                              v-if="
                                parseInt(comment.comment.userId) ===
                                parseInt($store.state.user.id)
                              "
                            >
                              删除
                            </button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    <div class="mb-3">
                      <label for="add-comment-content" class="form-label"
                        >内容</label
                      >
                      <textarea
                        v-model="comment_add.content"
                        class="form-control"
                        id="add-comment-content"
                        rows="3"
                        placeholder="请输入讨论内容"
                      ></textarea>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button
                      type="button"
                      class="btn btn-primary"
                      @click="add_comment(discuss.discuss.id)"
                    >
                      发表评论
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
            </div>
          </td>
          <td>
            <button
              @click="remove_discuss(discuss.discuss.id)"
              type="button"
              class="btn btn-danger"
              v-if="
                parseInt(discuss.discuss.userId) ===
                parseInt($store.state.user.id)
              "
            >
              删除
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <button
      type="button"
      class="btn btn-primary float-end"
      data-bs-toggle="modal"
      data-bs-target="#add-discuss-btn"
    >
      发表
    </button>
    <!-- Modal -->
    <div class="modal fade" id="add-discuss-btn" tabindex="-1">
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
              <label for="add-discuss-title" class="form-label">名称</label>
              <input
                v-model="discuss_add.title"
                type="text"
                class="form-control"
                id="add-discuss-title"
                placeholder="请输入讨论主题"
              />
            </div>
            <select
              class="form-select"
              aria-label="Default select example"
              v-model="discuss_add.type"
            >
              <option selected value="BLOG">分享</option>
              <option value="SOLUTION">题解</option>
              <option value="CHAT">吐槽</option>
              <option value="ADVICE">建议</option>
            </select>
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
              发表
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
    </div>
  </ContentField>
</template>
<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { ref, reactive } from "vue";
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
    const discuss_add = reactive({
      title: "",
      content: "",
      error_message: "",
    });

    let comments = ref([]);
    let current_comments_page = 1;
    let total_comments = 0;
    console.log(total_comments);
    const comment_add = reactive({
      content: "",
      error_message: "",
    });

    const pull_page = (page) => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/user/discuss/getList/",
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

    const add_discuss = () => {
      discuss_add.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/discuss/add/",
        type: "post",
        data: {
          title: discuss_add.title,
          type: discuss_add.type,
          content: discuss_add.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            discuss_add.title = "";
            discuss_add.type = "";
            discuss_add.content = "";
            Modal.getInstance("#add-discuss-btn").hide();
            pull_page(current_page);
          } else {
            discuss_add.error_message = resp.error_message;
          }
        },
      });
    };

    const remove_discuss = (discussId) => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/discuss/remove/",
        type: "post",
        data: {
          discuss_id: discussId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.error_message === "success") {
            pull_comments_page(current_page, discussId);
          }
        },
      });
    };

    const pull_comments_page = (page, discussId) => {
      current_comments_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/user/comment/getList/",
        data: {
          page: page,
          discuss_id: discussId,
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          comments.value = resp.comments;
          total_comments = resp.comments_count;
          //   update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    const add_comment = (discussId) => {
      comment_add.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/comment/add/",
        type: "post",
        data: {
          discuss_id: discussId,
          content: comment_add.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            comment_add.content = "";
            pull_comments_page(current_comments_page, discussId);
          } else {
            comment_add.error_message = resp.error_message;
          }
        },
      });
    };

    const remove_comment = (commentId, discussId) => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/comment/remove/",
        type: "post",
        data: {
          comment_id: commentId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.error_message === "success") {
            pull_comments_page(current_comments_page, discussId);
          }
        },
      });
    };

    return {
      discusses,
      discuss_add,
      add_discuss,
      remove_discuss,
      comments,
      comment_add,
      current_comments_page,
      pull_comments_page,
      add_comment,
      remove_comment,
    };
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/discuss/add/",
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
    //   url: "http://127.0.0.1:3000/user/discuss/remove/",
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
    //   url: "http://127.0.0.1:3000/user/discuss/getList/",
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
    //   url: "http://127.0.0.1:3000/user/discuss/update/",
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
img.comment-user-photo {
  width: 4vh;
  border-radius: 50%;
}
</style>
