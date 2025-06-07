import { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Intro from './components/Intro';
import Login from './pages/Login';
import Register from './pages/Register';
import Account from './pages/Account';
import CourseDetailPage from './pages/CourseDetailPage';
function App() {
  const [user, setUser] = useState(null);

  return (
    <>
      <Header user={user} />


      <Routes>
        <Route path="/" element={<Intro />} />
        <Route path="/course/:id" element={<CourseDetailPage />} />
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/account" element={<Account user={user} />} />
      </Routes>
    </>
  );
}

export default App;