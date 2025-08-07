const {createUserDocManually} = require("./create-user-doc");

/**
 * Firebase Auth onCreate 트리거 핸들러
 * @param {Object} user Firebase User Record
 */
module.exports = async (user) => {
  await createUserDocManually(user);
};
