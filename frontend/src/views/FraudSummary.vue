<script setup>
import { ref, onMounted } from "vue";
import api from "../services/api";

const summary = ref([]);
const error = ref(null);

// Format large numbers nicely (e.g. 33000000 → 33,000,000)
const formatAmount = (value) => {
  return Number(value).toLocaleString("en-IN");
};

onMounted(async () => {
  try {
    const res = await api.get("/fraud/summary");
    summary.value = res.data;
  } catch (err) {
    error.value = "Failed to load fraud summary";
    console.error("Fraud summary error:", err);
  }
});
</script>

<template>
  <div>
    <h2>Fraud Summary Report</h2>

    <p v-if="error" style="color: red">
      {{ error }}
    </p>

    <table
      v-if="summary.length > 0"
      border="1"
      cellpadding="6"
      cellspacing="0"
    >
      <thead>
        <tr>
          <th>Account</th>
          <th>Region</th>
          <th>Fraud Count</th>
          <th>Total Fraud Amount</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="s in summary" :key="s.accountId + '-' + s.region">
          <td>{{ s.accountId }}</td>
          <td>{{ s.region }}</td>
          <td>{{ s.fraudCount }}</td>
          <td>{{ formatAmount(s.totalFraudAmount) }}</td>
        </tr>
      </tbody>
    </table>

    <p v-else>
      No fraud summary data available.
    </p>
  </div>
</template>
