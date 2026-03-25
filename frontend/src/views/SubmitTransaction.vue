<script setup>
import { ref } from "vue";
import api from "../services/api";

const transaction = ref({
  id: "",
  accountId: "",
  amount: "",
  location: "",
  timestamp: ""
});

const result = ref(null);
const error = ref(null);
const loading = ref(false);

async function submitTransaction() {
  error.value = null;
  result.value = null;
  loading.value = true;

  try {
    const res = await api.post("/fraud/check", {
      ...transaction.value,
      amount: Number(transaction.value.amount)
    });

    result.value = res.data;
  } catch (err) {
    error.value = "Failed to submit transaction";
    console.error(err);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div>
    <h2>Submit Transaction</h2>

    <form @submit.prevent="submitTransaction">
      <div>
        <label>Transaction ID</label><br />
        <input v-model="transaction.id" required />
      </div>

      <div>
        <label>Account ID</label><br />
        <input v-model="transaction.accountId" required />
      </div>

      <div>
        <label>Amount</label><br />
        <input type="number" v-model="transaction.amount" required />
      </div>

      <div>
        <label>Location</label><br />
        <input v-model="transaction.location" required />
      </div>

      <div>
        <label>Timestamp</label><br />
        <input type="datetime-local" v-model="transaction.timestamp" required />
      </div>

      <br />
      <button type="submit" :disabled="loading">
        {{ loading ? "Checking..." : "Submit" }}
      </button>
    </form>

    <p v-if="error" style="color:red">{{ error }}</p>

    <div v-if="result" style="margin-top:20px">
      <h3>Result</h3>

      <p v-if="result.reasons.length === 0" style="color:green">
        Transaction Approved
      </p>

      <div v-else style="color:red">
        Fraud Detected
        <ul>
          <li v-for="r in result.reasons" :key="r">{{ r }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>
