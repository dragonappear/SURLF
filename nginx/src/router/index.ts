import { createRouter, createWebHistory } from "vue-router";
import IndexView from "../views/IndexView.vue";
import StatisticsView from "../views/StatisticsView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "index",
      component: IndexView,
    },
    {
      path: "/statistics",
      name: "statistics",
      component: StatisticsView,
    }
  ],
});

export default router;
