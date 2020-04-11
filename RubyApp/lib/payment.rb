#
class Payment
  attr_reader :name, :surname, :patron, :type, :debts
  attr_accessor :amount_paid
  def initialize (name, surname, patron, type, debts, amount_paid)
    @name = name
    @surname = surname
    @patron = patron
    @type = type
    @debts = debts.to_i
    @amount_paid = amount_paid.to_i
  end
   
   def == (other)
     @name == other.name && @surname == other.surname && @patron == other.patron && @type == other.type && @debts == other.debts && @amount_paid == other.amount_paid
   end
   
end