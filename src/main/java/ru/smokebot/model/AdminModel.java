package ru.smokebot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smokebot.dto.tables.Admins;
import ru.smokebot.repository.jpa.AdminPanelRepository;

import java.util.List;

@Service
public class AdminModel
{
    @Autowired private AdminPanelRepository adminPanel;

    private List<Admins> getAdminsList()
    {
        return adminPanel.findAll();
    }

    private boolean isAdminAtList(Long tgId)
    {
        return getAdminsList().stream().anyMatch(it -> it.getTgId().equals(tgId));
    }
    public Admins getAdmin(Long tgId)
    {
        if (isAdminAtList(tgId))
            for (Admins admin : getAdminsList())
                if (admin.getTgId().equals(tgId))
                    return admin;
        return null;
    }

    public void addAdmin(Admins adm)
    {
        if (!isAdminAtList(adm.getTgId())) adminPanel.save(adm);
    }

    public void removeAdmin(Admins adm)
    {
        if (isAdminAtList(adm.getTgId()))  adminPanel.deleteById(adm.getTgId());
    }
}
