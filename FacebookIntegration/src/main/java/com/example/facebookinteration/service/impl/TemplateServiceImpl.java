package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.entity.PageTemplate;
import com.example.facebookinteration.entity.SystemVariable;
import com.example.facebookinteration.repository.PageTemplateRepository;
import com.example.facebookinteration.repository.SystemVariableRepository;
import com.example.facebookinteration.service.TemplateService;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.constant.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private SystemVariableRepository variableRepo;
    @Autowired
    private PageTemplateRepository templateRepo;
    @Autowired
    private PageRepository pageRepository;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");

    @Override
    public List<SystemVariable> getAllVariables() {
        return variableRepo.findAll();
    }

    @Override
    public PageTemplate saveTemplate(String pageId, String templateContent) {
        // Kiểm tra page_id tồn tại
        if (pageRepository.findByPageId(pageId).isEmpty()) {
            throw new CustomException(404, "Page not found");
        }
        // 1. Trích xuất biến từ templateContent
        Set<String> variablesInTemplate = new HashSet<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
        while (matcher.find()) {
            variablesInTemplate.add(matcher.group(1));
        }
        // 2. Lấy biến hợp lệ từ DB
        Set<String> validVariables = variableRepo.findAll().stream()
            .map(SystemVariable::getVariableKey)
            .collect(Collectors.toSet());
        // 3. Xác thực
        for (String var : variablesInTemplate) {
            if (!validVariables.contains(var)) {
                throw new CustomException(400, "Biến '{{" + var + "}}' không được hệ thống hỗ trợ.");
            }
        }
        // 4. Lưu template
        PageTemplate template = new PageTemplate();
        template.setPageId(pageId);
        template.setTemplateContent(templateContent);
        return templateRepo.save(template);
    }
} 