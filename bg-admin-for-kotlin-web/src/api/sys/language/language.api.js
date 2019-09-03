import {fetch} from '../../base';

// 获取语言的切换的下拉菜单的数据
export const getLanguageSelect = params => {
  return fetch('/language/getLanguageSelect', params);
};

export const addLanguage = params => {
  return fetch('/language/addLanguage', params);
};
export const deleteLanguage = params => {
  return fetch('/language/deleteLanguage', params);
};
export const updateLanguage = params => {
  return fetch('/language/updateLanguage', params);
};
export const queryLanguageList = params => {
  return fetch('/language/queryLanguageList', params);
};
export const getLanguage = params => {
  return fetch('/language/getLanguage', params);
};
