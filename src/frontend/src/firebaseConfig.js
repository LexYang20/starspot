// src/firebaseConfig.js
import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

// 🔥 你的 Firebase 配置
const firebaseConfig = {
  apiKey: "AIzaSyD1LrMP_F8gOoYr93RMjCwPtQ0p8DW7Enw",
  authDomain: "aurora-e55fb.firebaseapp.com",
  databaseURL: "https://aurora-e55fb-default-rtdb.firebaseio.com",
  projectId: "aurora-e55fb",
  storageBucket: "aurora-e55fb.firebasestorage.app",
  messagingSenderId: "271092006617",
  appId: "1:271092006617:web:4fc865ace2f6affd6008c8",
  measurementId: "G-ECZJ5V8CX9"
};

// 🔥 初始化 Firebase
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

export { db };
// // Import the functions you need from the SDKs you need
// import { initializeApp } from "firebase/app";
// import { getAnalytics } from "firebase/analytics";
// // TODO: Add SDKs for Firebase products that you want to use
// // https://firebase.google.com/docs/web/setup#available-libraries

// // Your web app's Firebase configuration
// // For Firebase JS SDK v7.20.0 and later, measurementId is optional


// // Initialize Firebase
// const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);