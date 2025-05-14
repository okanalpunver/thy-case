import React, { createContext, useState, useContext, useEffect } from "react";
import api from "../Api";

const ErrorContext = createContext();

export function ErrorProvider({ children }) {
  const [error, setError] = useState(null);

  useEffect(() => {
    const interceptor = api.interceptors.response.use(
      (resp) => resp,
      (err) => {
        setError(
          err.response?.data?.message ||
            err.response?.data?.error ||
            err.message ||
            "Unexpected error"
        );
        return Promise.reject(err);
      }
    );
    return () => api.interceptors.response.eject(interceptor);
  }, []);

  const clearError = () => setError(null);

  return (
    <ErrorContext.Provider value={{ error, clearError }}>
      {children}
    </ErrorContext.Provider>
  );
}

export function useError() {
  return useContext(ErrorContext);
}
