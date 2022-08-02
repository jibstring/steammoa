import React from 'react';
import { NavLink } from "react-router-dom";

const Sidebar = (props) => {
  const userName = props.user.userServiceId

  const menus = [
    {title:'나의 파티',
      submenus: [
      {name:'참여 중인 파티', path:`/profile/${userName}/curparty`}, 
      {name:'종료된 파티', path:`/profile/${userName}/pastparty`}]},
    {title:'나의 글',
      submenus: [
      {name: '내가 쓴 리뷰글', path:`/profile/${userName}/myreview`},
      {name:'내가 쓴 공략글', path:`/profile/${userName}/mywalkthrough`} ]},
    ]
  return (
    <div className='h-screen bg-sidebar-light w-per25' >
      {/* 프로필 */}
      <NavLink
        to={`/profile/${userName}`}
      >
      <div className='p-5 bg-sidebar-dark hover:cursor-pointer'>
        <div className='flex justify-around items-center'>
          <img src="../../ImgAssets/TierGold.png" alt="" className='w-9'/>
          <span className='text-sm font-blackSans text-white'>{userName}</span>
        </div>
      </div>
      </NavLink>
      {/* 메뉴 */}
      <div>
      {menus.map((menu) => {
        return (
          <div>
            <div className=''>{menu.title}</div>
            {menu.submenus.map((submenu)=>{
              return (
                <div>    
                  <NavLink 
                    // style={{color: "gray", textDecoration: "none"}}
                    className={({isActive}) => (isActive ? "text-white": "text-gray-600")}
                    to={submenu.path}>
                      {submenu.name}
                  </NavLink>
                </div> 
                  )
            })}
          </div>
        )
      }) 
      }
      </div>
    </div>
  )
}

export default Sidebar