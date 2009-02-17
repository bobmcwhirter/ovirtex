# This file is auto-generated from the current state of the database. Instead of editing this file, 
# please use the migrations feature of Active Record to incrementally modify your database, and
# then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your database schema. If you need
# to create the application database on another system, you should be using db:schema:load, not running
# all the migrations from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20090217195310) do

  create_table "certificates", :force => true do |t|
    t.integer  "user_id"
    t.string   "dn",         :limit => 1024,  :null => false
    t.string   "material",   :limit => 32768, :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "email_addresses", :force => true do |t|
    t.integer  "user_id"
    t.string   "address",    :limit => 128, :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "public_keys", :force => true do |t|
    t.integer  "user_id"
    t.string   "name",        :limit => 64,   :null => false
    t.string   "material",    :limit => 5120, :null => false
    t.string   "fingerprint", :limit => 128,  :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "users", :force => true do |t|
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "display_name", :limit => 100,                    :null => false
    t.string   "password",     :limit => 100,                    :null => false
    t.boolean  "admin",                       :default => false
  end

end
