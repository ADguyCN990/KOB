import { createRouter, createWebHistory } from 'vue-router'
import NotFoundView from '../views/error/NotFoundView.vue'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import UserAccountLoginIndexView from '../views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterIndexView from '../views/user/account/UserAccountRegisterView.vue'

const routes = [
  {
    path: "/",
    name: "home",
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
  },
  {
    path: "/ranklist/",
    name: "rank_index",
    component: RanklistIndexView,
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginIndexView,
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterIndexView,
  },
  {
    path: "/404/",
    name: "404_index",
    component: NotFoundView,
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

export default router
