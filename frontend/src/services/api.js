import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:30090/api",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (!token) {
    throw new axios.Cancel("No token");
  }

  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;
