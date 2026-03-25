### Fraud Detection Logic

The system flags a transaction as fraudulent based on:

- Amount Threshold  
  Transactions above a predefined limit are marked suspicious  

- Velocity Check  
  Multiple transactions within a short time window (e.g., >5 transactions in 30 seconds)  

- Rule-Based Patterns  
  - Transactions from unusual locations  
  - Rapid repeated transactions  
  - High-risk merchant categories  

Each transaction is evaluated in real-time and assigned a fraud status.
