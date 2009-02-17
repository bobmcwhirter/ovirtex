class PublicKey < ActiveRecord::Base

  belongs_to :user

  def to_param
    self.name
  end

  def to_s
    self.name
  end
end
