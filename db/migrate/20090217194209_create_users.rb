class CreateUsers < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      t.timestamps
      t.string :display_name, :limit=>100, :null=>false
      t.string :password, :limit=>100, :null=>false
      t.boolean :admin, :default=>false
    end
  end

  def self.down
    drop_table :users
  end
end
