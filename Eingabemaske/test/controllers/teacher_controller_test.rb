require 'test_helper'

class TeacherControllerTest < ActionDispatch::IntegrationTest
  test "should get main" do
    get teacher_main_url
    assert_response :success
  end

  test "should get login" do
    get teacher_login_url
    assert_response :success
  end

end
