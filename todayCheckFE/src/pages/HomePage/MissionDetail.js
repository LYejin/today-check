import React, { useState, useEffect } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';

const missionData = {
  postPicture: 'https://via.placeholder.com/350x200',
  adminName: '이예진',
  adminPicture: 'https://via.placeholder.com/350x200',
  participants: 3,
  postTitle: '코딩테스트 공부하실 분',
  postContent:
    '코딩테스트 매일 하나씩 풀 예정입니다. 함께 하실 분 모집합니다. 열심히 하실 분만 참여해주세요. 라고한번 적어봅니다 그냥 한분이라도 와주시면 감사',
};

const userData = [
  {
    id: 1,
    avater: 'https://via.placeholder.com/350x200',
    userName: '이예진',
    image: 'https://via.placeholder.com/350x200',
  },
  {
    id: 1,
    avater: 'https://via.placeholder.com/350x200',
    userName: '이예진',
    image: 'https://via.placeholder.com/350x200',
  },
  {
    id: 1,
    avater: 'https://via.placeholder.com/350x200',
    userName: '이예진',
    image: 'https://via.placeholder.com/350x200',
  },
  {
    id: 1,
    avater: 'https://via.placeholder.com/350x200',
    userName: '이예진',
    image: 'https://via.placeholder.com/350x200',
  },
];

const MissionDetail = () => {
  const [missionDetail, setMissionDetail] = useState([]);
  const [join, setJoin] = useState(false);
  const params = useParams();
  const paramsData = params.id;
  const startDate = String(missionDetail.startDate).slice(0, 10);
  const endDate = String(missionDetail.endDate).slice(0, 10);

  useEffect(() => {
    axios
      .get(`/mission/${paramsData}`, { id: paramsData })
      .then(response => {
        console.log(response);
        setMissionDetail(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  console.log(missionDetail);
  console.log(userData.map(user => user.userName));

  return (
    <MissionWrapper>
      <MissionHeader>
        <MissionImage>
          <img src={missionDetail.postPicture} />
        </MissionImage>
        <MissionInfBox>
          <MissionTitle>{missionDetail.postTitle}</MissionTitle>
          <MissionContent>{missionDetail.postContent}</MissionContent>
          <MissionDate>
            기간: {startDate} ~ {endDate}
          </MissionDate>
          {join ? (
            <CompletionButton onClick={() => setJoin(!join)}>
              참여완료🌈
            </CompletionButton>
          ) : (
            <MissionButton onClick={() => setJoin(!join)}>참여중</MissionButton>
          )}
        </MissionInfBox>
      </MissionHeader>

      <ParticipantWrapper>
        {missionDetail.participants?.map(props => (
          <ParticipantBox key={props.id}>
            <ParticipantInfo>
              <ParticipantImage>
                <img src={props.avater} />
              </ParticipantImage>
              <ParticipantName>{props.name}</ParticipantName>
            </ParticipantInfo>
            <Picture>
              <img src={props.image} />
            </Picture>
          </ParticipantBox>
        ))}
      </ParticipantWrapper>
    </MissionWrapper>
  );
};

export default MissionDetail;

const MissionHeader = styled.div`
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 97%;
  height: 45%;
  border-radius: 10px;
  background-color: #9090f9;
`;

const MissionWrapper = styled.section`
  position: relative;
  width: 100vw;
  height: 100vh;
  background-color: #eeeeff;
  overflow: scroll;
`;

const MissionImage = styled.div`
  img {
    width: 350px;
    height: 230px;
  }
`;

const MissionInfBox = styled.div`
  display: block;
  width: 18rem;
  height: 16rem;
  margin-left: 0.8rem;
  background-color: #6a6ab5;
  border-radius: 5px;
`;

const MissionTitle = styled.span`
  display: block;
  width: 100%;
  height: 48px;
  overflow: hidden;
  text-align: center;
  color: white;
  font-size: 1.4rem;
  margin-top: 1.5rem;
  font-weight: bold;
`;

const MissionContent = styled.span`
  display: block;
  margin: 0 auto;
  color: white;
  margin-top: 0.5rem;
  text-align: center;
  width: 250px;
  height: 6rem;
  font-size: 1rem;
`;

const MissionDate = styled.div`
  color: white;
  margin: 0 auto;
  text-align: center;
  width: 250px;
`;

const MissionButton = styled.button`
  display: block;
  margin: 0 auto;
  text-align: center;
  width: 250px;
  font-size: 1rem;
  height: 2.2rem;
  border-radius: 5px;
  margin-top: 0.5rem;
  background-color: #eeeeff;
`;

const CompletionButton = styled.button`
  display: block;
  margin: 0 auto;
  text-align: center;
  width: 250px;
  font-size: 1rem;
  height: 2.2rem;
  border-radius: 5px;
  margin-top: 0.5rem;
  background-color: #6a6ab5;
`;

const ParticipantWrapper = styled.div`
  display: flex;
  justify-content: space-evenly;
  align-content: center;
  flex-flow: row wrap;
  flex-grow: 2;
  width: 100%;
  padding: 0 2rem;
`;

const ParticipantBox = styled.div`
  margin-top: 3rem;
  display: block;
  width: 230px;
  height: 300px;
  border-top: 5px solid gray;
  padding-top: 1rem;
`;

const ParticipantInfo = styled.div`
  display: flex;
  justify-content: start;
  align-items: center;
`;

const ParticipantImage = styled.div`
  display: inline;
  margin-left: 2px;
  img {
    width: 30px;
    height: 30px;
    border-radius: 100%;
  }
`;

const ParticipantName = styled.span`
  margin-left: 5px;
`;

const Picture = styled.div`
  margin-top: 2rem;
  img {
    width: 100%;
    height: 200px;
  }
`;
