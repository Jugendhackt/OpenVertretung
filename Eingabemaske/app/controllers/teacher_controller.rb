class TeacherController < ApplicationController
  protect_from_forgery with: :null_session
  def main
  end

  def login
  end

  def create
    render json: params
  end
end
