import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";

const SearchBar = (props) => {
  const { search, onChangeSearch } = props;
  return (
    <div
      id="search-bar"
      className="col-span-3 flex felx-row bg-searchbar-gray rounded-lg"
    >
      <div className="flex w-per5 inset-y-0 left-0 items-center pl-3 pointer-events-none">
        <FontAwesomeIcon className="text-detailContent-light" icon={faSearch} />
      </div>
      <input
        type="text"
        id="search"
        className="w-per80 mx-2 text-sm text-gray-900 bg-transparent border-none focus:outline-hidden focus:border-none "
        placeholder="모아글을 검색하세요"
        value={search}
        onChange={onChangeSearch}
      />
      <button className="w-per15 px-1 py-1 my-1 mr-1 block text-sm text-white bg-mainBtn-blue hover:bg-mainBtn-blue-hover rounded-lg ">
        검색
      </button>
    </div>
  );
};

export default SearchBar;
