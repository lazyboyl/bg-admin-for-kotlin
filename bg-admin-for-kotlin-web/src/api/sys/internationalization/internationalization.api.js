import {fetch} from '../../base';

// 初始化国际化信息
export const initInternationalization = params => {
  return fetch('/internationalization/initInternationalization',params);
};

// 实现获取国际化信息
export const getInternationalizationById = params => {
  return fetch('/internationalization/getInternationalizationById',params);
};

// 实现更新国际化信息
export const updateInternationalize = params => {
  return fetch('/internationalization/updateInternationalize',params);
};

// 实现删除国际化信息
export const deleteInternationalization = params => {
  return fetch('/internationalization/deleteInternationalization',params);
};

// 实现新增国际化信息
export const addInternationalize = params => {
  return fetch('/internationalization/addInternationalize',params);
};

// 获取当前需要维护的语言的类型
export const getLanguage = params => {
  return fetch('/internationalization/getLanguage',params);
};

// 获取国际化树
export const getInternationalizationTree = params => {
  return fetch('/internationalization/getInternationalizationTree',params);
};
