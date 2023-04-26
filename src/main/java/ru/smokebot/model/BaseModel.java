package ru.smokebot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.smokebot.bot.ConfigureBot;
import ru.smokebot.dto.tables.Admins;
import ru.smokebot.dto.tables.MainBase;
import ru.smokebot.dto.tables.ReferalsList;
import ru.smokebot.repository.jpa.MainBaseRepository;
import ru.smokebot.repository.jpa.ReferalsListRepository;

@Service
public class BaseModel {
    @Autowired private MainBaseRepository mainBaseRepository;
    @Autowired private ReferalsListRepository referalsListRepository;

    @Autowired private AdminModel adminModel;

    /**
     * @param telegramId Параметр - айди телеграма отправителя.
     *                   Функция добавляет нового юзера в mainbase
     *                   только тогда, когда его там нет.
     */
    public void addToMainBase(Long telegramId) {
        boolean state = mainBaseRepository
                .findAll()
                .stream()
                .anyMatch(it -> it.getTelegramId().equals(telegramId));
        if (!state) mainBaseRepository.save(new MainBase(telegramId));
    }

    /**
     * @param refId Параметр - айди рефера-отца
     * @param tgId  Параметр - телеграм айди отправителя.
     *              Функция добавляет в базу рефоф tgId только тогда, когда
     *              его там нет.
     */
    private void addToRefList(Long refId, Long tgId) {
        boolean state = alreadyHasRefId(tgId);
        if (!state) referalsListRepository.save(new ReferalsList(refId, tgId, 0L, 0L));
    }

    /**
     * @param id Параметр - айди отца-рефа
     * @return Возвращает true/false в зависимости от того,
     * существует ли параметр id в mainbase
     */
    public boolean idIsExists(Long id) {
        return mainBaseRepository.findAll().stream().anyMatch(it -> it.getMainId().equals(id));
    }

    /**
     * @param tgId Данная функция отвечает за занесение в базу данных
     *             новых польльзователей, которые не перешли по реферальной
     *             ссылке. В качестве дефолтного айди указывается
     *             "ConfigureBot.uniqueId"
     */
    public void registerNewRef(Long tgId) {
        addToRefList(ConfigureBot.uniqueId, tgId);
    }

    /**
     * @param tgId Параметр функции - телеграм айди дочернего рефера
     * @return Функция предназначена для поиска совпадений, те
     * функция ищет в базе данных информацию о том,
     * является ли данный телеграм айди уже дочерним для
     * какого-то родителя-рефера
     */
    private boolean alreadyHasRefId(Long tgId) {
        return referalsListRepository
                .findAll()
                .stream()
                .anyMatch(it -> it.getChildId().equals(tgId));
    }


    /**
     * @param tgId Айди телеграмма отправителя
     * @return Возвращает true/false  в зависимости от того,
     * имеется ли данный пользователь в БД рефералов.
     */
    public boolean isAtReferalBase(Long tgId) {
        return alreadyHasRefId(tgId);
    }

    /**
     * @param message Данная функция отвечает за занесение в базу данных
     *                новых польльзователей и занесение в базу данных рефералов,
     *                если не нарушены ограничения. А именно:
     *                1. Юзер пытается переподключить отца-рефера. метод "alreadyHasRefId"
     *                пытается этого не допустить.
     *                2. В базу рефоф пользователь будет занесен только в том случае,
     *                если ссылка действительна. А она действительна только тогда,
     *                когда айди отца-рефера существует в базе данных.
     */
    public void registerNewRef(Message message) {
        StringBuffer buffer = new StringBuffer();
        for (char t : message.getText().toCharArray())
            if (Character.isDigit(t))
                buffer.append(t);

        Long refId = Long.valueOf(buffer.toString());
        Long telegramId = message.getChatId();
        addToMainBase(telegramId);

        if (alreadyHasRefId(telegramId)) return;
        if (findIdByTg(telegramId).equals(refId)) return;
        if (idIsExists(refId)) addToRefList(refId, telegramId);
    }

    /**
     * @param tgId Параметр - телеграм айди отправителя
     * @return Возвращает main_id из mainbase по параметру
     */
    public Long findIdByTg(Long tgId) {
        return mainBaseRepository
                .findAll()
                .stream()
                .filter(it -> it.getTelegramId().equals(tgId))
                .findFirst()
                .get()
                .getMainId();
    }

    /**
     *
     * @param mainId
     * Параметр - айти телеграм - отправителя
     * @return
     * Возвращает телеграм-айди, если такой
     * существует.
     */
    public Long findTgById(Long mainId)
    {
        return mainBaseRepository
                    .findAll()
                    .stream()
                    .filter(it-> it.getMainId().equals(mainId))
                    .findFirst()
                    .get()
                    .getTelegramId();

    }

    /**
     * @param message Параметр - сообщение от отправителя
     * @return Возвращает строку - ссылка на тг бот + main_id из mainbase.
     * (Реферальная ссылка). ВАЖНО! Используется именно main_id,
     * а не айди телеграмма.(Для удобства)
     */
    public String getRefLink(Message message) {
        Long telegramId = message.getChatId();
        Long id = findIdByTg(telegramId);
        return ConfigureBot.refLink + id;
    }


    /**
     * @return Функция, которая возвращает айди своего отца-рефера.
     * В аргументе принимает айди телеграмма человека,
     * который выполняет запрос.
     */
    public Long getFatherId(Long tgId) {
        return referalsListRepository
                .findAll()
                .stream()
                .filter(it -> it.getChildId().equals(tgId))
                .findFirst()
                .get()
                .getMainId();
    }


    /**
     * @param tgId Параметр - телеграм айди пользователя
     * @return Возвращает размер скидки по параметру
     * (пользователю)
     */
    public Long getDiscount(Long tgId) {
        return referalsListRepository.findById(tgId).get().getDiscountSize();
    }

    public void changeDiscountSize(Long tgId, Long discountChangeSize)
    {
        ReferalsList note = referalsListRepository.findById(tgId).get();
        Long discount = note.getDiscountSize();
        discount -= discountChangeSize;
        note.setDiscountSize(discount);
        referalsListRepository.save(note);
    }

    /**
     *
     * @param mainId
     * Параметр - айди отца - рефера
     * @param tgId
     * Параметр - айди отца - рефера
     * @return
     * Функция возвращает количество заказов,
     * которые сделали дети отца из параметров.
     */
    private Long getAmountOfAllOrders(Long mainId, Long tgId) {
        Long amount = 0L;
        for (ReferalsList note : referalsListRepository.findByOrderByMainIdAsc())
            if (note.getMainId().equals(mainId) && !note.getChildId().equals(tgId))
                amount += note.getCountBought();

        return amount;
    }

    /**
     *
     * @param tgId
     * Параметр - телеграм айди совершившего покупку.
     * Функция увеличивает количество покупок
     * для данного ребенка на 1 из таблицы
     * referalslist.
     */
    private void increaseOrdersCount(Long tgId, Long number)
    {
        ReferalsList list = referalsListRepository.findById(tgId).get();
        if (number == null)   list.setCountBought(list.getCountBought() + 1);
        else                  list.setCountBought(list.getCountBought() + number);
        referalsListRepository.save(list);
    }

    /**
     *
     * @param tgId
     * Параметр - телеграм айди совершившего покупку
     * @param orderAmount
     * Функция увеличивает количество покупок, совершаемое
     * пользователем и увеличивает размер скидки для отца
     * детей, которые совершили покупку.
     */
    public void increaseDiscountAndCount (Long tgId, Long orderAmount)
    {
        Long fatherId = getFatherId(tgId);
        Long fatherTgId = findTgById(fatherId);
        //Проверка на то, является ли главный айди менеджером (надо дать админку)
        Admins admin = adminModel.getAdmin(fatherTgId);
        increaseOrdersCount(tgId, null);
        if (admin != null)
        {

            if (!tgId.equals(fatherTgId)) adminSetDiscount(fatherTgId, orderAmount);
            return ;

        }

        long commonAmount = getAmountOfAllOrders(fatherId, fatherTgId);
        double percent = 0.0;
        if (commonAmount == 1)   percent = 5;
        else
        {
            for (int i = 0; i < commonAmount; i++)
            {
                if (i == 0) percent = 5;
                else        percent += 2.5;
            }
        }

        if (percent > 20.0) percent = 20.0;

        ReferalsList note = referalsListRepository.findById(fatherTgId).get();
        Long previousDiscount = note.getDiscountSize();      // скидка, которая уже была
        long addingDiscount = (long)((orderAmount / 100.0) * percent); // добавляемая скидка от текущей сделки
        note.setDiscountSize(previousDiscount + addingDiscount);
        referalsListRepository.save(note);
    }

    /**
     *
     * @param tgId
     * Параметр - телеграм - айди отца - рефера
     * @param orderAmount
     * Параметр - стоимость покупки, совершенной ребёнком
     * Функция высчитывает 10% от стоимости покупки,
     * совершённой ребёнком и начислят их администратору.
     */
    private void adminSetDiscount(Long tgId, Long orderAmount)
    {
        ReferalsList note = referalsListRepository.findById(tgId).get();
        Long previousAmount = note.getDiscountSize();
        Double newAmount = (orderAmount / 100.0) * 10;
        note.setDiscountSize((long) (previousAmount + newAmount));
        referalsListRepository.save(note);
    }

    /**
     *
     * @param tgId
     * Параметр - телеграм айди совершившего покупку
     * @param currentAmount
     * Параметр - текущая цена из корзины (без скидки)
     * @return
     * Функция возвращает стоимость корзины со скидкой
     */
    public Long getDiscountPrice(Long tgId, Long currentAmount, boolean discountSwitchedOn)
    {
        long price;
        if (discountSwitchedOn) price = currentAmount - getDiscount(tgId);
        else                    price = currentAmount;

        if (hasPersonFirstOrder(tgId))  price = (long) (price - (currentAmount / 10.0 * 2));
        if (price < 0)  return 0L;
        else            return price;
    }

    /**
     *
     * @param mainId
     * Параметр - айди рефера, у которого изменяют отца-рефера
     * @param mainId2
     * Параметр - отец - рефер, на который изменяют
     * @return
     * Возвращает true/false в зависимости от того,
     * поменялся ли отец - рефер у заданного пользователя или нет.
     */
    public boolean changeRef(Long mainId, Long mainId2)
    {
        if (idIsExists(mainId) && idIsExists(mainId2))
        {
            Long tgId = findTgById(mainId);
            Long tgId2 = findTgById(mainId2);
            if (isAtReferalBase(tgId) && isAtReferalBase(tgId2))
            {
                ReferalsList note = referalsListRepository.findById(tgId).get();
                note.setMainId(mainId2);
                referalsListRepository.save(note);
                return true;
            }
            return false;

        }
        return false;
    }

    /**
     *
     * @param mainId
     * Параметр - айди пользователя, у которого меняют скидку
     * @param amount
     * Параметр - общая сумма изменяемой скидки
     * @return
     * Вовзвращает true / false в зависимости от того,
     * удалось ли изменить скидку у пользователя или нет.
     */
    public boolean changePercent(Long mainId, Long amount)
    {
        if (idIsExists(mainId))
        {
            Long tgId = findTgById(mainId);
            if (isAtReferalBase(tgId))
            {
                ReferalsList note = referalsListRepository.findById(tgId).get();
                note.setDiscountSize(amount);
                referalsListRepository.save(note);
                return true;
            }
            else return false;
        }
        return false;
    }

    /**
     *
     * @param tgId
     * Параметр - телеграм айди покупателя
     * @return
     * Возвращает true/false в зависимости от того,
     * является ли данная покупка первой у клиента.
     */
    private boolean hasPersonFirstOrder(Long tgId)
    {
        Long commonOrders = referalsListRepository.findById(tgId).get().getCountBought();
        return commonOrders == 0;
    }

}
