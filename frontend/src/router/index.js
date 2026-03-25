import { createRouter, createWebHistory } from "vue-router";

import LoginView from "../views/LoginView.vue";
import AuthCallback from "../views/AuthCallback.vue";
import HomeView from "../views/HomeView.vue";
import FraudAlerts from "../views/FraudAlerts.vue";
import FraudSummary from "../views/FraudSummary.vue";
import SubmitTransaction from "../views/SubmitTransaction.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "login",
      component: LoginView, // ✅ LOGIN IS ROOT
    },
    {
      path: "/callback",
      name: "callback",
      component: AuthCallback,
    },
    {
      path: "/dashboard",
      name: "dashboard",
      component: HomeView,
      meta: { requiresAuth: true },
    },
    {
      path: "/fraud",
      component: FraudAlerts,
      meta: { requiresAuth: true },
    },
    {
      path: "/summary",
      component: FraudSummary,
      meta: { requiresAuth: true },
    },
    {
      path: "/submit",
      component: SubmitTransaction,
      meta: { requiresAuth: true }
    },
  ],
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  if (to.meta.requiresAuth && !token) {
    next("/");
    return;
  }

  if (to.path === "/" && token) {
    next("/dashboard");
    return;
  }

  next();
});

export default router;
