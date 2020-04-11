require "minitest/autorun"
require 'minitest/unit'
require 'list_payments'
require 'payment'
#
class TestGroup < Minitest::Test
  def setup
    @payments = ListPayments.new([
                               Payment.new('Ivan', 'Ivanov', 'Sergeevich', 'rent',110, 80),
                               Payment.new('Ivan', 'Ivanov','Sergeevich', 'electric power', 100, 0),
                               Payment.new('Ivan', 'Ivanov', 'Sergeevich', 'telephone charges', 120, 0),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'rent', 100, 40),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'electric power', 120, 0),
                               Payment.new('Sergey', 'Sidorov', 'Vasilyevich', 'rent', 100, 40),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'telephone charges', 90, 10),
                               Payment.new('Artem', 'Smirnov', 'Andreevich', 'telephone charges', 90, 10)])
  end
  
  def test_add_new
    @payments << Payment.new('Ivan', 'Ivanov','Sergeevich', 'electric power', 100, 0)
    assert_equal @payments.amount_payments, 9
  end
  
  def test_index
    person = Payment.new('Ivan', 'Ivanov','Sergeevich', 'electric power', 100, 0)
    assert_equal person, @payments[1]
  end
  
  def test_page_maker
    list = @payments.page_maker(1)
    person = Payment.new('Sergey', 'Sidorov', 'Vasilyevich', 'rent', 100, 40)
    assert_equal list[1][:data], person
  end
  
  def test_sort
    payments = ListPayments.new([
                               Payment.new('Artem', 'Smirnov', 'Andreevich', 'telephone charges', 90, 10),
                               Payment.new('Ivan', 'Ivanov','Sergeevich', 'electric power', 100, 0),
                               Payment.new('Ivan', 'Ivanov', 'Sergeevich', 'telephone charges', 120, 0),
                               Payment.new('Ivan', 'Ivanov', 'Sergeevich', 'rent',110, 80),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'rent', 100, 40),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'electric power', 120, 0),
                               Payment.new('Sergey', 'Petrov', 'Vasilyevich', 'telephone charges', 90, 10),
                               Payment.new('Sergey', 'Sidorov', 'Vasilyevich', 'rent',100, 40)
                               ])
    @payments.sort
    @payments.list.each.with_index do |el, index|
      assert_equal el, payments[index]
    end
  end
  
  def test_page_count
    assert_equal @payments.page_count, 3
  end
  
  
end
