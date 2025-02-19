import React from 'react';
import styled from 'styled-components';
import { HiUserGroup } from 'react-icons/hi';
import { Link } from 'react-router-dom';

const HomeBox = ({
  id,
  postPicture,
  adminName,
  adminPicture,
  participants,
  postTitle,
  postContent,
}) => {
  return (
    <HomeBoxContainer>
      {/* admin user 정보  */}
      <HomeBoxHead>
        <img src={adminPicture} alt="postPicture" />
        <span>{adminName}</span>
      </HomeBoxHead>

      {/*  main picture */}
      <HomeBoxPicture>
        <img src={postPicture} alt="" />
      </HomeBoxPicture>

      {/* title, content */}
      <HomeBoxTopic>
        <HomeBoxTitle>{postTitle}</HomeBoxTitle>
        <HomeBoxContent>{postContent}</HomeBoxContent>
      </HomeBoxTopic>

      {/* 하단 참여수 및 참여하기 버튼 */}
      <HomeBoxBottom>
        <HomeBoxParticipants>
          <HiUserGroup className="HomeBoxUserIcon" />
          {participants} 참여중
        </HomeBoxParticipants>
        <Link to={`/missionDetail/${id}`}>
          <HomeBoxButton>참여하기</HomeBoxButton>
        </Link>
      </HomeBoxBottom>
    </HomeBoxContainer>
  );
};

export default HomeBox;

const HomeBoxContainer = styled.div`
  width: 240px;
  height: 300px;
  border-radius: 5px;
  background-color: white;
  margin-top: 20px;
  margin-left: 1rem;
  margin-right: 1rem;
`;

const HomeBoxHead = styled.div`
  display: flex;
  justify-content: start;
  align-items: center;
  margin: 3px 0;
  padding-left: 5px;
  width: 100%;
  height: 25px;
  font-size: 12px;
  img {
    width: 25px;
    height: 25px;
    margin-right: 5px;
    border-radius: 100%;
  }
`;

const HomeBoxPicture = styled.div`
  img {
    width: 100%;
    height: 170px;
  }
`;

const HomeBoxTopic = styled.div`
  width: 100%;
  height: 60px;
`;

const HomeBoxTitle = styled.div`
  margin: 3px 0;
  font-weight: bold;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`;

const HomeBoxContent = styled.div`
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
`;

const HomeBoxBottom = styled.div`
  display: flex;
  width: 100%;
  height: 33px;
  align-items: center;
  border-top: solid 2px #dbd9d9;
`;

const HomeBoxParticipants = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  height: 30px;
  color: #a5a5a5;
  font-size: 14px;

  .HomeBoxUserIcon {
    margin-left: 5px;
    margin-right: 5px;
    font-size: 18px;
  }
`;

const HomeBoxButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 34px;
  background-color: #bfbfff;
  &:active,
  &:hover {
    background-color: #a5a5ff;
  }
`;
