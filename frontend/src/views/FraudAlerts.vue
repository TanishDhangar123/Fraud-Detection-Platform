<script setup>
import { ref, onMounted } from "vue";
import api from "../services/api";

const frauds = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const res = await api.get("/transactions/fraud");
    frauds.value = res.data;
  } catch (err) {
    error.value = "Failed to load fraud alerts";
    console.error(err);
  }
});
</script>

<template>
  <div>
    <h2 style="color: red"> Fraud Alerts</h2>

    <p v-if="error" style="color: red">{{ error }}</p>

    <table border="1" cellpadding="6">
      <thead>
        <tr>
          <th>ID</th>
          <th>Account</th>
          <th>Amount</th>
          <th>Location</th>
          <th>Timestamp</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="t in frauds"
          :key="t.id"
          style="background-color: #ffe5e5"
        >
          <td>{{ t.id }}</td>
          <td>{{ t.accountId }}</td>
          <td>{{ t.amount }}</td>
          <td>{{ t.location }}</td>
          <td>{{ t.timestamp }}</td>
        </tr>
      </tbody>
    </table>

    <p v-if="frauds.length === 0">No fraud detected</p>
  </div>
</template>
