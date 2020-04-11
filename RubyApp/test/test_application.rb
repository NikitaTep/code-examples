require_relative 'helper'
require 'capybara/minitest'
##
# Tests for the main flat page operations
class TestFlatLogic < MiniTest::Test
  include Capybara::DSL
  
  def setup
    @app = Sinatra::Application.new
    Capybara.app = @app
  end
  
  def teardown
    Capybara.reset_sessions!
    Capybara.use_default_driver
  end
  
  def test_add_new_payment
    visit '/'
    click_on 'Платежи'
    click_on 'Добавить новый платеж'
    fill_in( "name", with: 'Artem')
    fill_in('surname', with: 'Sidorov')
    fill_in('patron', with: 'Alekseevich')
    select('Rent', from: 'type')
    fill_in('debts', with: 100)
    fill_in('amount_paid', with: 0 )
    click_on('Добавить')
    assert page.has_content?('Artem'), 'Name should be added'
    assert page.has_content?('Sidorov'), 'Surname should be added'
  end
  
end