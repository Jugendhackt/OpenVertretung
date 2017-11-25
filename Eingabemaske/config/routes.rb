Rails.application.routes.draw do
  get 'teacher/main'
  get 'teacher/login'
  match '/request' => 'teacher#create', via: :post
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
