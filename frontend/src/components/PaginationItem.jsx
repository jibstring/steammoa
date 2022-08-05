import React from "react";

const PaginationItem = (props) => {
  const { value, handlePageMove, active } = props;

  const onClickItem = () => {
    handlePageMove(value);
  };

  return (
    <li
      onClick={onClickItem}
      className={`${
        active ? "bg-main-300" : ""
      } w-7 h-7  text-lg text-white text-center list-none inline-block rounded-full hover:bg-main-100 hover:text-main-500`}
    >
      {value}
    </li>
  );
};

export default PaginationItem;
