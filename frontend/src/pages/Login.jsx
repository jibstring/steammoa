import React, { useState } from "react";
import { Link } from "react-router-dom";
// import { useUserContext } from './user-context';
import Navbar from "../components/Navbar.jsx";
import axios from "axios";

const Login = (props) => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");

  const onChange = (e) => {
    if (e.target.id === "input_id") setId(e.target.value);
    else setPassword(e.target.value);
  };

  const login = () => {
    axios
      .post("URL", {
        user_service_id: id,
        user_service_pw: password,
      })
      .then(function (response) { //실패했을때 코드
          console.log(response);
        //   로그인 후 처리 -> 
        //   1. 중앙 state 보관
        //   2. main OR 원래 페이지로 redirect(원래 있던 페이지 저장)
        

      })
        .catch(() => {
          
      });
  };

  return (
    <>
      <Navbar></Navbar>
      <div className="bg-slate-700 w-4/5 h-screen m-auto mb-6 flex flex-col align-center justify-center">
        <div className="m-auto w-96">
            <div className="login-logo">
            <img src="../ImgAssets/TypoIconLogo.png" alt="TypoIconLogo" className="login-Logo m-auto my-10"/>
            </div>
            <div className="login-form">
              <input
                className="bg-gray-50 mb-4 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                id="input_id"
                onChange={onChange}
                type="text"
                value={id}
                placeholder="아이디를 입력하세요"
              />
              <input
                className="bg-gray-50 mb-4 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                id="input_password"
                onChange={onChange}
                type="password"
                value={password}
                placeholder="비밀번호를 입력하세요"
              />
              <div className="login-find-account flex justify-end texts-end text-white text-xs mb-4">
                <div className="mr-2">
                <Link to="/" >회원가입</Link>
                </div>
                <div className="items-end mb-4">
                <Link to="/">아이디|비밀번호 찾기</Link>
                </div>
              </div>
            </div>
            <div>
              <button className="login-button text-white bg-black hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg w-full h-16 px-5 py-2.5 text-center" onClick={login}>
                로그인
              </button>
            </div>
        </div>
      </div>
      
    </>
  );
};

export default Login;
