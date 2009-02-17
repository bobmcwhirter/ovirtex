class CreatePublicKeys < ActiveRecord::Migration
  def self.up
    create_table :public_keys do |t|
      t.belongs_to :user
      t.string :name,        :limit=>64,   :null=>false
      t.string :material,    :limit=>5120, :null=>false
      t.string :fingerprint, :limit=>128,  :null=>false
      t.timestamps
    end
  end

  def self.down
    drop_table :public_keys
  end
end
