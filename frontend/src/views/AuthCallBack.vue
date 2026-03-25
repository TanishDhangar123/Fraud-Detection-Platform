<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

onMounted(async () => {
  const params = new URLSearchParams(window.location.search);
  const code = params.get("code");

  if (!code) {
    localStorage.clear();
    router.replace("/");
    return;
  }

  try {
    const response = await axios.post(
      "http://localhost:8080/realms/fraud-realm/protocol/openid-connect/token",
      new URLSearchParams({
        grant_type: "authorization_code",
        client_id: "fraud-api-client",
        code,
        redirect_uri: "http://localhost:5173/callback",
      }),
      { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
    );

    localStorage.setItem("token", response.data.access_token);
    localStorage.setItem("id_token", response.data.id_token);
    router.replace("/dashboard");
  } catch (e) {
    localStorage.clear();
    router.replace("/");
  }
});
</script>

<template>
  <p style="text-align:center;margin-top:50px">Logging in…</p>
</template>
