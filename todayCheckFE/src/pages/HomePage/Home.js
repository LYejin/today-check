import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import HomeBox from '../../components/home/homeBox';

const homeData = [
  {
    id: 1,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 2,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 3,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 4,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 5,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 6,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 7,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
  {
    id: 8,
    postPicture: 'https://via.placeholder.com/350x200',
    admin: { name: '이예진', avater: 'https://via.placeholder.com/350x200' },
    participants: ['이', '이이', '이이이'],
    postTitle: '코딩테스트 공부하실 분',
    postContent:
      '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다.',
  },
];

const Home = () => {
  const [missions, setMissions] = useState([]);
  const iconv = require('iconv-lite');

  useEffect(() => {
    axios({
      url: '/mission',
      method: 'get',
      // responseType: 'arraybuffer',
    })
      .then(response => {
        // const decoded = iconv.decode(response, 'EUC-KR');
        // console.log(decoded);
        setMissions(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  return (
    <RightContainer>
      <HomeBoxContainer>
        {missions.map(data => (
          <HomeBox
            key={data.id}
            id={data.id}
            postPicture={data.postPicture}
            adminName={data.admin.name}
            adminPicture={data.admin.avater}
            participants={data.participants?.length}
            postTitle={data.postTitle}
            postContent={data.postContent}
          />
        ))}
      </HomeBoxContainer>
    </RightContainer>
  );
};

export default Home;

const RightContainer = styled.div`
  height: 100vh;
  width: 100vw;
  background-color: #eeeeff;
  overflow-x: hidden;
  overflow-y: scroll;
  padding-bottom: 1rem;
`;

const HomeBoxContainer = styled.div`
  display: flex;
  justify-content: space-evenly;
  align-content: center;
  flex-flow: row wrap;
  flex-grow: 2;
`;
