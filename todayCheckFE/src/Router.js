import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './pages/LoginPage/Login';
import Navigation from './components/Navigation';
import styled from 'styled-components';

//components
import Posting from './pages/PostingPage/Posting';
import LoginCallback from './pages/LoginPage/LoginCallback';
import NaverLoginCallback from './pages/LoginPage/NaverLoginCallback';
import Home from './pages/HomePage/Home';
import MissionDetail from './pages/HomePage/MissionDetail';
import PostingDetail from './pages/PostingPage/PostingDetail';
import Mypage from './pages/Mypage/Mypage';

const Router = () => {
  return (
    <BrowserRouter>
      <Container>
        <Navigation />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/posting" element={<Posting />} />
          <Route path="/mypage" element={<Mypage />} />
          <Route path="/missionDetail/:id" element={<MissionDetail />} />
          <Route path="/login/*" element={<Login />} />
          <Route path="/login/callback" element={<LoginCallback />} />
          <Route path="/login/navercallback" element={<NaverLoginCallback />} />
          <Route path="/posting" element={<Posting />} />
          <Route path="/postingDetail" element={<PostingDetail />} />
        </Routes>
      </Container>
    </BrowserRouter>
  );
};

export default Router;

const Container = styled.div`
  display: flex;
`;
