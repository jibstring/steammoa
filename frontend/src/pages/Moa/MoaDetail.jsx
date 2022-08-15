import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { moaDetail, moaJoin, moaLeave } from "../../api/Moazone";
import MoaUserCard from "../../components/Moa/MoaUserCard";
import MoaPartyEval from "../../components/Moa/MoaPartyEval";
import Navbar from "../../components/Navbar";
import { auth } from "../../recoil/Auth";
import { formatTimeISO } from "../../util/FormatTime";
import Swal from "sweetalert2";
import { getPartyEvalInfo } from "../../api/MoaPartyEval";
import { faL } from "@fortawesome/free-solid-svg-icons";

function MoaDetail() {
  const PARTY_ISPLAYING = "3";
  const user = useRecoilState(auth);
  const userId = user[0].userId;
  const params = useParams();
  const partyId = params.party_id;
  const navigate = useNavigate();

  const [leader, setLeader] = useState();
  const [isParticipant, setIsParticipant] = useState(false);
  const [showEvalModal, setShowEvalModal] = useState(false);

  const [detailMoa, setDetailMoa] = useState({
    gameId: 1,
    gameImgPath: "",
    gameName: "",
    partyId: partyId,
    partyTitle: "",
    partyTags: [],
    maxPlayer: 0,
    curPlayer: 0,
    startTime: "",
    writeTime: "",
    partyStatus: "",
    partyPlayers: [],
    partyDescription: "",
    chatLink: "",
    writerId: "",
    partyIsUrgent: false,
  });

  const [partyEvalInfo, setPartyEvalInfo] = useState({
    gameName: "",
    partyId: "",
    partyTitle: "",
    partyPlayers: [],
    evalCompleted: true,
  });

  const items = ["즐겜", "빡겜", "공략겜", "무지성겜", "친목겜"];

  useEffect(() => {
    moaDetail(partyId).then(({ data }) => {
      console.log("moaDetail 호출 후", data);
      setDetailMoa(data);

      const lst = [];
      data.partyTags.forEach((tag) => {
        const idx = items.findIndex((item) => item === tag);
        lst.push(`${idx + 1}`);
      });
      const users = [];
      data.partyPlayers.forEach((player) => {
        // users.push(player.userId)
        if (player.leader === true) {
          setLeader(player.userId);
        }
      });
    });
  }, []);

  // 평가 관련
  useEffect(() => {
    detailMoa.partyPlayers.map((player) => {
      if (player.userId === userId) {
        setIsParticipant(true);
      }
    });
  }, [detailMoa]);

  useEffect(() => {
    if (isParticipant && detailMoa.partyStatus === `4`) {
      getPartyEvalInfo(partyId, userId)
        .then((res) => {
          console.log(res);
          const newEvalInfo = {
            gameName: res.data.party.gameName,
            partyId: res.data.party.partyId,
            partyTitle: res.data.party.partyTitle,
            partyPlayers: res.data.party.partyPlayers,
            evalCompleted: res.data.party.evalCompleted,
          };
          setPartyEvalInfo(newEvalInfo);
        })
        .catch((err) => console.log(err));
    }
  }, [isParticipant]);

  useEffect(() => {
    if (!partyEvalInfo.evalCompleted) {
      setShowEvalModal(true);
    }
  }, [partyEvalInfo]);

  const handlePartyJoin = (e) => {
    e.preventDefault();
    if (!userId) {
      Swal.fire({
        position: "center",
        icon: "warning",
        title: "먼저 로그인을 해 주세요!",
        showConfirmButton: false,
        timer: 1500,
      });
      navigate(`/login`);
    } else {
      moaJoin(detailMoa, partyId, userId).then((res) => {
        console.log("참여하기 누른 뒤", res);
        setDetailMoa(res.data);
      });
    }
  };

  const handlePartyUpdate = (e) => {
    e.preventDefault();
    navigate(`/moazone/update/${partyId}`);
  };

  const handlePartyLeave = (e) => {
    e.preventDefault();
    moaLeave(detailMoa, partyId, userId).then((res) => {
      setDetailMoa(res.data);
    });
  };

  const handlePartyShare = (e) => {
    e.preventDefault();
    const link = window.location.href;
    navigator.clipboard.writeText(link);
    Swal.fire({
      position: "center",
      icon: "success",
      title: "링크가 클립보드에 복사되었습니다.",
      showConfirmButton: false,
      timer: 1500,
    });
  };

  const handlePrevPage = (e) => {
    e.preventDefault();
    navigate(`/moazone`);
  };

  const playerList = [];
  detailMoa.partyPlayers.forEach((player) => {
    playerList.push(player.userId);
  });

  const onDeleteUser = (deleteUserId) => {
    //파티원 삭제 모달창
    Swal.fire({
      title: "파티원을 정말 강퇴시키겠습니까?",
      icon: "warning",
      position: "center",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "네. 퇴장시킬래요",
      cancelButtonText: "취소",
    }).then((result) => {
      if (result.isConfirmed) {
        moaLeave(detailMoa, partyId, deleteUserId);
        setDetailMoa((detailMoa) => {
          const newPlayerList = detailMoa.partyPlayers.filter(
            (player) => player.userId !== deleteUserId
          );

          return { ...detailMoa, partyPlayers: newPlayerList };
        });
      }
    });
  };

  let statusMsg = ["마감임박", "모집중", "모집완료", "게임중", "게임완료", "모집실패"];

  let bgColors = [
    "bg-red-400",
    "bg-moa-pink",
    "bg-moa-green",
    "bg-moa-yellow",
    "bg-moa-purple",
    "bg-mainBtn-disabled",
  ];

  const checkChat = () => {
    if (detailMoa.partyStatus === PARTY_ISPLAYING && isPlayer()) return true;
    return false;
  };
  const isPlayer = () => {
    const found = detailMoa.partyPlayers.find((player) => player.userId === userId);
    return found ? true : false;
  };

  const neon = {
    border: "#fff",
    boxShadow:
      "0 0 7px #fff, 0 0 10px #fff, 0 0 21px #fff, 0 0 42px #03a9f4, 0 0 82px #03a9f4, 0 0 92px #03a9f4, 0 0 102px #03a9f4, 0 0 151px #03a9f4",
  };

  const onClickChat = () => {
    navigate(`/chat/${partyId}`);
  };

  const checkCanUpdate = () => {
    return parseInt(detailMoa.partyStatus) < 3 ? true : false;
  };

  return (
    <div>
      <Navbar />
      <div className="w-per75 h-screen m-auto mb-2 text-white font-sans">
        <div className="overflow-hidden w-full relative pb-[25%] bg-gray-900 object opacity-[95%] hover:opacity-100 transition-transform ease-in-out duration-7000">
          {/* 게임 이미지 */}
          <img
            className="w-screen absolute top-[-50%] left-0 hover:scale-[55%] hover:translate-y-[5%] hover:object-contain transition-transform delay-150 ease-in-out duration-700"
            src={detailMoa.gameImgPath}
            alt="게임 이미지"
          />
        </div>
        {/* 게임 이름 */}
        <div className="w-full laptop:h-8 tablet:h-5 bg-gradient-to-b from-bg-search-gradient-from via-bg-search-gradient-via to-bg-search-gradient-to font-blackSans text-xl whitespace-nowrap text-ellipsis">
          {detailMoa.gameName}
        </div>
        {/* 본문 */}
        <div className="p-[2.5%] mb-4">
          <div className="flex justify-between">
            <div className="flex justify-between">
              {/* 파티 모집 상태 */}
              <div
                className={`px-4 rounded flex items-center justify-center w-per55 font-blackSans text-white
                ${detailMoa.partyIsUrgent ? bgColors[0] : bgColors[detailMoa.partyStatus]}`}>
                <span>
                  {detailMoa.partyIsUrgent ? statusMsg[0] : statusMsg[detailMoa.partyStatus]}
                </span>
              </div>
              {/* 파티 제목 */}
              <div
                className="mx-4 mt-2 font-blackSans items-center text-xl tablet:text-2xl laptop:text-[32px] text-whitetext-base whitespace-nowrap text-ellipsis"
                name="partyTitle"
                value={detailMoa.partyTitle}>
                {detailMoa.partyTitle}
              </div>
            </div>
            {checkChat() ? (
              <button
                className="hover:cursor-pointer hover:scale-[102%] h-9 p-2 rounded-2xl text-[2vw] tablet:text-[1.1vw] laptop:text-sm font-semibold bg-[#03a9f4]"
                style={neon}
                onClick={onClickChat}>
                채팅 입장하기
              </button>
            ) : (
              <div className="flex">
                {checkCanUpdate() ? (
                  detailMoa.writerId === userId ? (
                    <button
                      className=" h-9 hover:cursor-pointer text-white rounded-2xl font-semibold text-[2vw] tablet:text-[1.1vw] laptop:text-sm px-1.5 tablet:px-2.5 py-0.5 bg-moa-yellow-dark hover:bg-moa-yellow drop-shadow-lg hover:scale-[102%] text-center flex items-center "
                      onClick={handlePartyUpdate}>
                      파티 수정하기
                    </button>
                  ) : playerList.includes(userId) ? (
                    <button
                      className=" h-9 hover:cursor-pointer text-white rounded-2xl font-semibold text-[2vw] tablet:text-[1.1vw] laptop:text-sm px-1.5 tablet:px-2.5 py-0.5 bg-moa-pink hover:bg-moa-pink-dark drop-shadow-lg hover:scale-[102%] text-center flex items-center "
                      onClick={handlePartyLeave}>
                      {" "}
                      파티 나가기{" "}
                    </button>
                  ) : (
                    <button
                      className=" h-9 hover:cursor-pointer text-white rounded-2xl font-semibold text-[2vw] tablet:text-[1.1vw] laptop:text-sm px-1.5 tablet:px-2.5 py-0.5 bg-moa-yellow-dark hover:bg-moa-yellow drop-shadow-lg hover:scale-[102%] text-center flex items-center "
                      onClick={handlePartyJoin}>
                      파티 참여하기
                    </button>
                  )
                ) : (
                  ""
                )}
              </div>
            )}
          </div>

          <div className="flex justify-between items-center my-2">
            <div className="flex">
              {detailMoa.partyTags.map((item, idx) => {
                return (
                  <div
                    key={idx}
                    name="partyTags"
                    className="h-7 rounded-2xl font-semibold text-white text-[2vw] tablet:text-[1.1vw] laptop:text-sm px-2.5 py-1 bg-moa-green-dark drop-shadow-lg text-center flex items-center mr-2">
                    {item}
                  </div>
                );
              })}
            </div>
            <div className="flex">
              {checkCanUpdate() ? (
                <button
                  className=" h-9 hover:cursor-pointer text-white rounded-2xl font-semibold text-[2vw] tablet:text-[1.1vw] laptop:text-sm px-1.5 tablet:px-2.5 py-0.5 bg-moa-blue hover:bg-moa-blue-dark drop-shadow-lg hover:scale-[102%] text-center flex items-center"
                  onClick={handlePartyShare}>
                  파티 공유하기
                </button>
              ) : (
                ""
              )}
            </div>
          </div>
          <hr />
          {detailMoa.partyPlayers.length !== 0 && (
            <div className="my-3 text-xl font-blackSans" name="startTime">
              파티 시작 시간 : {formatTimeISO(detailMoa.startTime)}{" "}
            </div>
          )}
          <div className="text-xl font-blackSans my-3">
            참가 파티원 ({detailMoa.curPlayer}/{detailMoa.maxPlayer})
          </div>
            <div className="flex flex-wrap justify-start items-center overflow-auto p-5">
              {detailMoa.partyPlayers.map((player, playerId) => {
                return (
                  <MoaUserCard
                    key={playerId}
                    player={player}
                    leader={leader}
                    deleteUser={onDeleteUser}
                  />
                );
              })}
            </div>
          <hr />
          <div className="font-blackSans text-xl my-3">파티 모집 내용</div>
          <div className="w-full h-80 px-2 py-1 tablet:px-3 tablet:py-2 laptop:px-5 laptop:py-3 tablet rounded opacity-90 bg-detailContent-light text-black overflow-ellipse overflow-y-scroll">
            {" "}
            {detailMoa.partyDescription}
          </div>
          <div className="grid place-items-center mt-4">
            <button
              onClick={handlePrevPage}
              className="w-32 h-12 mx-3 bg-mainBtn-blue hover:bg-mainBtn-blue-hover rounded-lg text-sm">
              파티 목록 보기
            </button>
          </div>
        </div>
      </div>

      <MoaPartyEval
        showEvalModal={showEvalModal}
        setShowEvalModal={setShowEvalModal}
        partyEvalInfo={partyEvalInfo}
        userId={userId}></MoaPartyEval>
    </div>
  );
}

export default MoaDetail;
