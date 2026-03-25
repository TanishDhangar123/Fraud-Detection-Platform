<script setup>
import { computed } from "vue";
import { useRoute, useRouter, RouterView } from "vue-router";

const route = useRoute();
const router = useRouter();

/**
 * Navbar should appear ONLY on protected pages
 */
const showNavbar = computed(() => {
  return route.meta.requiresAuth === true;
});

function logout() {
  const idToken = localStorage.getItem("id_token");

  localStorage.clear();

  const logoutUrl =
    "http://localhost:8080/realms/fraud-realm/protocol/openid-connect/logout" +
    "?id_token_hint=" + idToken +
    "&post_logout_redirect_uri=http://localhost:5173/";

  window.location.href = logoutUrl;
}


</script>

<template>
  <div>
    <!-- ✅ NAVBAR ONLY FOR AUTHENTICATED ROUTES -->
    <nav v-if="showNavbar" style="text-align:center; margin-bottom:20px">
      <router-link to="/dashboard">All Transactions</router-link> |
      <router-link to="/fraud">Fraud Alerts</router-link> |
      <router-link to="/summary">Fraud Summary</router-link> |
      <router-link to="/submit">Submit Transaction</router-link> |
      <button @click="logout">Logout</button>
    </nav>

    <RouterView />
  </div>
</template>

<style scoped>
nav {
  margin-top: 20px;
}
button {
  margin-left: 10px;
}
</style>
