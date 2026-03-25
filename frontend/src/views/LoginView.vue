<script setup>
function login() {
  const authUrl =
    "http://localhost:8080/realms/fraud-realm/protocol/openid-connect/auth" +
    "?client_id=fraud-api-client" +
    "&response_type=code" +
    "&scope=openid" +
    "&redirect_uri=http://localhost:5173/callback";

  window.location.href = authUrl;
}
</script>

<template>
  <div style="text-align:center;margin-top:100px">
    <h2>Fraud Dashboard Login</h2>
    <p>You must login via Keycloak</p>
    <button @click="login">Login with Keycloak</button>
  </div>
</template>
