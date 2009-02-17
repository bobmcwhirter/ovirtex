class UsersController < ApplicationController

  before_filter :load_user, :only=>[ :show ]


  def new
    @user = User.new
    @email_address = EmailAddress.new
  end

  def create
    user = User.new( params[:user] )
    ActiveRecord::Base.transaction do 
      user.save!
      email_address = EmailAddress.new( :address=>params[:email_address] )
      email_address.user = user
      email_address.save!
    end
    redirect_to user
  end

  def index
    @users = User.find( :all, :order=>:display_name ) 
  end

  def show
  end

  private

  def load_user
    @user = User.find_by_id( params[:id] )
    redirect_to users_url and return false unless @user
  end

end
