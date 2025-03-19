import axios from "axios";

const API_URL = "/api/query"; // Adjust the endpoint as per your backend

const QueryHandler = {
  createNew: async () => {
    try {
      const response = await axios.get(`${API_URL}?method=new`);
      return response.data;
    } catch (error) {
      console.error("Error creating new entry:", error);
      return null;
    }
  },

  updateRow: async (id) => {
    try {
      const response = await axios.get(`${API_URL}?method=update&id=${id}`);
      return response.data;
    } catch (error) {
      console.error("Error updating entry:", error);
      return null;
    }
  },

  deleteRow: async (id) => {
    try {
      const response = await axios.get(`${API_URL}?method=delete&id=${id}`);
      return response.data;
    } catch (error) {
      console.error("Error deleting entry:", error);
      return null;
    }
  },

  getAll: async () => {
    try {
      const response = await axios.get(`${API_URL}?method=get-all`);
      return response.data;
    } catch (error) {
      console.error("Error fetching all entries:", error);
      return [];
    }
  },

  getSingle: async (id) => {
    try {
      const response = await axios.get(`${API_URL}?method=get-single&id=${id}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching single entry:", error);
      return null;
    }
  },
};

export default QueryHandler;
