import React, { createContext, useState, useContext, useEffect } from "react";
import api from "../Api";

const LoadingContext = createContext();

export function LoadingProvider({ children }) {
  const [count, setCount] = useState(0);

  useEffect(() => {
    const reqInterceptor = api.interceptors.request.use(
      (config) => {
        setCount((c) => c + 1);
        return config;
      },
      (err) => {
        setCount((c) => Math.max(c - 1, 0));
        return Promise.reject(err);
      }
    );

    const resInterceptor = api.interceptors.response.use(
      (resp) => {
        setCount((c) => Math.max(c - 1, 0));
        return resp;
      },
      (err) => {
        setCount((c) => Math.max(c - 1, 0));
        return Promise.reject(err);
      }
    );

    return () => {
      api.interceptors.request.eject(reqInterceptor);
      api.interceptors.response.eject(resInterceptor);
    };
  }, []);

  return (
    <LoadingContext.Provider value={{ isLoading: count > 0 }}>
      {children}
    </LoadingContext.Provider>
  );
}

export function useLoading() {
  return useContext(LoadingContext);
}
