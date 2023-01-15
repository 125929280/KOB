<template>
  <ContentField>
    <button
        type="button"
        class="btn btn-primary float-end"
        data-bs-toggle="modal"
        data-bs-target="#add-discuss-btn"
    >
      发表
    </button>
    <!-- 发表评论 Modal -->
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
                  rows="15"
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
    <table
        class="table table-striped table-hover"
        style="text-align: center; table-layout: fixed"
    >
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
          <router-link
              :to="{
                name: 'discuss_content',
                params: { discussId: discuss.discuss.id },
              }"
          >{{ discuss.discuss.title }}
          </router-link
          >
        </td>
        <td>
          <img :src="discuss.photo" alt="" class="discuss-user-photo"/>
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
          <!-- 查看讨论 Modal -->
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
                      class="table"
                      style="text-align: center; table-layout: fixed"
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
                  <nav aria-label="...">
                    <ul class="pagination" style="float: right">
                      <li
                          class="page-item"
                          @click="click_comment_page(-2, discuss.discuss.id)"
                      >
                        <a class="page-link" href="#">前一页</a>
                      </li>
                      <li
                          :class="'page-item ' + page.is_active"
                          v-for="page in comment_pages"
                          :key="page.number"
                          @click="
                            click_comment_page(page.number, discuss.discuss.id)
                          "
                      >
                        <a class="page-link" href="#">{{ page.number }}</a>
                      </li>
                      <li
                          class="page-item"
                          @click="click_comment_page(-1, discuss.discuss.id)"
                      >
                        <a class="page-link" href="#">后一页</a>
                      </li>
                    </ul>
                  </nav>
                  <div class="mb-3">
                    <label
                        for="add-comment-content"
                        class="form-label"
                    ></label>
                    <textarea
                        v-model="comment_add.content"
                        class="form-control"
                        id="add-comment-content"
                        rows="3"
                        placeholder="请输入评论内容"
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
              v-if="discuss.discuss.userId === parseInt($store.state.user.id)"
          >
            删除
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="...">
      <ul class="pagination" style="float: right">
        <li class="page-item" @click="click_discuss_page(-2)">
          <a class="page-link" href="#">前一页</a>
        </li>
        <li
            :class="'page-item ' + page.is_active"
            v-for="page in pages"
            :key="page.number"
            @click="click_page(page.number)"
        >
          <a class="page-link" href="#">{{ page.number }}</a>
        </li>
        <li class="page-item" @click="click_discuss_page(-1)">
          <a class="page-link" href="#">后一页</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>
<script>
import ContentField from "../../components/ContentField.vue";
import {useStore} from "vuex";
import {Modal} from "bootstrap/dist/js/bootstrap";
import {ref, reactive} from "vue";
import $ from "jquery";

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const discussUrl = ref("http://127.0.0.1:8081/user/discuss/");
    let discusses = ref([]);
    let current_page = 1;
    let total_discusses = 0;
    let pages = ref([]);
    const discuss_add = reactive({
      title: "",
      type: "BLOG",
      content: "",
      error_message: "",
    });

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_discusses / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };

    const click_page = (page) => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_discusses / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };

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
          update_pages();
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
            discuss_add.type = "BLOG";
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
            pull_page(current_page);
          }
        },
      });
    };

    let comments = ref([]);
    let current_comments_page = 1;
    let total_comments = 0;
    let comment_pages = ref([]);
    const comment_add = reactive({
      content: "",
      error_message: "",
    });

    const update_comment_pages = () => {
      let max_pages = parseInt(Math.ceil(total_comments / 10));
      let new_pages = [];
      if (max_pages === 0) {
        new_pages.push({
          number: 1,
          is_active: "active",
        });
        comment_pages.value = new_pages;
        return;
      }
      for (
          let i = current_comments_page - 2;
          i <= current_comments_page + 2;
          i++
      ) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_comments_page ? "active" : "",
          });
        }
      }
      comment_pages.value = new_pages;
    };

    const click_comment_page = (page, discussId) => {
      if (page === -2) page = current_comments_page - 1;
      else if (page === -1) page = current_comments_page + 1;
      let max_pages = parseInt(Math.ceil(total_comments / 10));
      if (page >= 1 && page <= max_pages) {
        pull_comments_page(page, discussId);
      }
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
          update_comment_pages();
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
      discussUrl,
      discusses,
      discuss_add,
      pages,
      click_page,
      add_discuss,
      remove_discuss,
      comments,
      comment_add,
      current_comments_page,
      comment_pages,
      click_comment_page,
      pull_comments_page,
      add_comment,
      remove_comment,
    };
  },
};
</script>
<style scoped>
a {
  text-decoration: none;
}

img.discuss-user-photo {
  width: 4vh;
  border-radius: 50%;
}

img.comment-user-photo {
  width: 4vh;
  border-radius: 50%;
}

div.error-message {
  color: red;
}
</style>
