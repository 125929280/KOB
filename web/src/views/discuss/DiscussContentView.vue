<template>
  <ContentField>
    <div class="row">
      <h2 class="style margin-top:10px">{{ discuss.discuss.title }}</h2>
      <div class="row">
        <div class="col-3">
          <div class="card" style="margin-top: 20px">
            <div class="card-body">
              <img :src="discuss.photo" alt="" class="discuss-user-photo"/>
              &nbsp;
              <span class="discuss-user-username">{{ discuss.username }}</span>
            </div>
          </div>
        </div>
        <div class="col-9">
          <div class="card" style="margin-top: 20px">
            <div class="card-body">
              <span class="discuss-user-username">{{
                  discuss.discuss.content
                }}</span>
            </div>
          </div>
        </div>
      </div>
      <table class="table" style="text-align: center; table-layout: fixed">
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
            <img :src="discuss.photo" alt="" class="discuss-user-photo"/>
            &nbsp;
            <span class="discuss-user-username">{{ discuss.username }}</span>
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
        <tr v-for="comment in comments" :key="comment.comment.id">
          <td>
            <img :src="comment.photo" alt="" class="comment-user-photo"/>
            &nbsp;
            <span class="comment-user-username">{{ comment.username }}</span>
          </td>
          <td>
            {{ comment.comment.content }}
          </td>
          <td>
            {{ comment.comment.createTime }}
          </td>
          <td>
            <button
                @click="remove_comment(comment.comment.id)"
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
        <label for="add-comment-content" class="form-label"></label>
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
      <button type="button" class="btn btn-primary" @click="add_comment()">
        发表评论
      </button>
    </div>
  </ContentField
  >
</template>
<script>
import ContentField from "../../components/ContentField.vue";
import {useStore} from "vuex";
import {ref, reactive} from "vue";
import $ from "jquery";
import {useRouter} from "vue-router";

export default {
  components: {
    ContentField,
  },
  setup() {
    const router = useRouter();
    let discussId = router.currentRoute.value.params.discussId;
    // let discuss = ref([]);
    let discuss = ref();
    const store = useStore();
    let comments = ref([]);
    let current_page = 1;
    let total_comments = 0;
    let pages = ref([]);
    const comment_add = reactive({
      content: "",
      error_message: "",
    });

    $.ajax({
      url: "http://127.0.0.1:3000/user/discuss/get/",
      data: {
        discuss_id: discussId,
      },
      type: "get",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        discuss.value = resp;
      },
      error(resp) {
        console.log(resp);
      },
    });

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_comments / 10));
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
      let max_pages = parseInt(Math.ceil(total_comments / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };

    const pull_page = (page) => {
      current_page = page;
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
          // console.log(resp);
          comments.value = resp.comments;
          total_comments = resp.comments_count;
          update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    pull_page(current_page);

    const add_comment = () => {
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
            pull_page(current_page);
          } else {
            comment_add.error_message = resp.error_message;
          }
        },
      });
    };

    const remove_comment = (commentId) => {
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
          // console.log(resp);
          if (resp.error_message === "success") {
            pull_page(current_page);
          }
        },
      });
    };
    return {
      comments,
      comment_add,
      pages,
      discuss,
      click_page,
      add_comment,
      remove_comment,
    };
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

div.error-message {
  color: red;
}
</style>
