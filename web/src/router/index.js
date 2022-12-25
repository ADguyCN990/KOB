import { createRouter, createWebHistory } from 'vue-router'
import NotFoundView from '../views/error/NotFoundView.vue'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import UserAccountLoginIndexView from '../views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterIndexView from '../views/user/account/UserAccountRegisterView.vue'
import UserAccountPhotoIndexView from '../views/user/account/UserAccountPhotoIndexView.vue'
import store from '../store/index'

const routes = [
  {
    path: "/",
    name: "home",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "rank_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/photo/",
    name: "user_photo_index",
    component: UserAccountPhotoIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginIndexView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterIndexView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/404/",
    name: "404_index",
    component: NotFoundView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }


]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const jwt_token = localStorage.getItem("jwt_token");

  let st = true;
  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getinfo", {
      success() { },
      error() {
        router.push({ name: "user_account_login" });
      },
    });
  } else {
    st = false;
  }

  if (to.meta.requestAuth && !store.state.user.is_login && !st) {
    next({ name: "user_account_login" });
  } else {
    next();
  }
});



export default router



