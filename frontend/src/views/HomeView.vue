<script setup>
import { ref, onMounted } from "vue";
import api from "../services/api";

const transactions = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const res = await api.get("/transactions");
    transactions.value = res.data;
  } catch (err) {
    error.value = "Failed to load transactions";
    console.error(err);
  }
});
</script>

<template>
  <div>
    <h2>All Transactions</h2>

    <p v-if="error" style="color: red">{{ error }}</p>

    <table border="1" cellpadding="6">
      <thead>
        <tr>
          <th>ID</th>
          <th>Account</th>
          <th>Amount</th>
          <th>Location</th>
          <th>Timestamp</th>
          <th>Status</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="t in transactions" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.accountId }}</td>
          <td>{{ t.amount }}</td>
          <td>{{ t.location }}</td>
          <td>{{ t.timestamp }}</td>
          <td :style="{ color: t.flagged ? 'red' : 'green' }">
            {{ t.flagged ? "FRAUD" : "OK" }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
